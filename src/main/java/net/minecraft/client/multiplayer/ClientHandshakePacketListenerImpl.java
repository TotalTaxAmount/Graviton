package net.minecraft.client.multiplayer;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InsufficientPrivilegesException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.exceptions.UserBannedException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.logging.LogUtils;
import java.math.BigInteger;
import java.security.PublicKey;
import java.time.Duration;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.login.ClientLoginPacketListener;
import net.minecraft.network.protocol.login.ClientboundCustomQueryPacket;
import net.minecraft.network.protocol.login.ClientboundGameProfilePacket;
import net.minecraft.network.protocol.login.ClientboundHelloPacket;
import net.minecraft.network.protocol.login.ClientboundLoginCompressionPacket;
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
import net.minecraft.network.protocol.login.ServerboundCustomQueryPacket;
import net.minecraft.network.protocol.login.ServerboundKeyPacket;
import net.minecraft.realms.DisconnectedRealmsScreen;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.util.Crypt;
import net.minecraft.util.HttpUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.slf4j.Logger;

@OnlyIn(Dist.CLIENT)
public class ClientHandshakePacketListenerImpl implements ClientLoginPacketListener {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final Minecraft minecraft;
   @Nullable
   private final ServerData serverData;
   @Nullable
   private final Screen parent;
   private final Consumer<Component> updateStatus;
   private final Connection connection;
   private GameProfile localGameProfile;
   private final boolean newWorld;
   @Nullable
   private final Duration worldLoadDuration;

   public ClientHandshakePacketListenerImpl(Connection p_261697_, Minecraft p_261835_, @Nullable ServerData p_261938_, @Nullable Screen p_261783_, boolean p_261562_, @Nullable Duration p_261673_, Consumer<Component> p_261945_) {
      this.connection = p_261697_;
      this.minecraft = p_261835_;
      this.serverData = p_261938_;
      this.parent = p_261783_;
      this.updateStatus = p_261945_;
      this.newWorld = p_261562_;
      this.worldLoadDuration = p_261673_;
   }

   public void handleHello(ClientboundHelloPacket p_104549_) {
      Cipher cipher;
      Cipher cipher1;
      String s;
      ServerboundKeyPacket serverboundkeypacket;
      try {
         SecretKey secretkey = Crypt.generateSecretKey();
         PublicKey publickey = p_104549_.getPublicKey();
         s = (new BigInteger(Crypt.digestData(p_104549_.getServerId(), publickey, secretkey))).toString(16);
         cipher = Crypt.getCipher(2, secretkey);
         cipher1 = Crypt.getCipher(1, secretkey);
         byte[] abyte = p_104549_.getChallenge();
         serverboundkeypacket = new ServerboundKeyPacket(secretkey, publickey, abyte);
      } catch (Exception exception) {
         throw new IllegalStateException("Protocol error", exception);
      }

      this.updateStatus.accept(Component.translatable("connect.authorizing"));
      HttpUtil.DOWNLOAD_EXECUTOR.submit(() -> {
         Component component = this.authenticateServer(s);
         if (component != null) {
            if (this.serverData == null || !this.serverData.isLan()) {
               this.connection.disconnect(component);
               return;
            }

            LOGGER.warn(component.getString());
         }

         this.updateStatus.accept(Component.translatable("connect.encrypting"));
         this.connection.send(serverboundkeypacket, PacketSendListener.thenRun(() -> {
            this.connection.setEncryptionKey(cipher, cipher1);
         }));
      });
   }

   @Nullable
   private Component authenticateServer(String p_104532_) {
      try {
         this.getMinecraftSessionService().joinServer(this.minecraft.getUser().getGameProfile(), this.minecraft.getUser().getAccessToken(), p_104532_);
         return null;
      } catch (AuthenticationUnavailableException authenticationunavailableexception) {
         return Component.translatable("disconnect.loginFailedInfo", Component.translatable("disconnect.loginFailedInfo.serversUnavailable"));
      } catch (InvalidCredentialsException invalidcredentialsexception) {
         return Component.translatable("disconnect.loginFailedInfo", Component.translatable("disconnect.loginFailedInfo.invalidSession"));
      } catch (InsufficientPrivilegesException insufficientprivilegesexception) {
         return Component.translatable("disconnect.loginFailedInfo", Component.translatable("disconnect.loginFailedInfo.insufficientPrivileges"));
      } catch (UserBannedException userbannedexception) {
         return Component.translatable("disconnect.loginFailedInfo", Component.translatable("disconnect.loginFailedInfo.userBanned"));
      } catch (AuthenticationException authenticationexception) {
         return Component.translatable("disconnect.loginFailedInfo", authenticationexception.getMessage());
      }
   }

   private MinecraftSessionService getMinecraftSessionService() {
      return this.minecraft.getMinecraftSessionService();
   }

   public void handleGameProfile(ClientboundGameProfilePacket p_104547_) {
      this.updateStatus.accept(Component.translatable("connect.joining"));
      this.localGameProfile = p_104547_.getGameProfile();
      this.connection.setProtocol(ConnectionProtocol.PLAY);
      this.connection.setListener(new ClientPacketListener(this.minecraft, this.parent, this.connection, this.serverData, this.localGameProfile, this.minecraft.getTelemetryManager().createWorldSessionManager(this.newWorld, this.worldLoadDuration)));
   }

   public void onDisconnect(Component p_104543_) {
      if (this.parent != null && this.parent instanceof RealmsScreen) {
         this.minecraft.setScreen(new DisconnectedRealmsScreen(this.parent, CommonComponents.CONNECT_FAILED, p_104543_));
      } else {
         this.minecraft.setScreen(new DisconnectedScreen(this.parent, CommonComponents.CONNECT_FAILED, p_104543_));
      }

   }

   public boolean isAcceptingMessages() {
      return this.connection.isConnected();
   }

   public void handleDisconnect(ClientboundLoginDisconnectPacket p_104553_) {
      this.connection.disconnect(p_104553_.getReason());
   }

   public void handleCompression(ClientboundLoginCompressionPacket p_104551_) {
      if (!this.connection.isMemoryConnection()) {
         this.connection.setupCompression(p_104551_.getCompressionThreshold(), false);
      }

   }

   public void handleCustomQuery(ClientboundCustomQueryPacket p_104545_) {
      this.updateStatus.accept(Component.translatable("connect.negotiating"));
      this.connection.send(new ServerboundCustomQueryPacket(p_104545_.getTransactionId(), (FriendlyByteBuf)null));
   }
}