package dev.totaltax.particle.module.impl.player;

import com.mojang.blaze3d.platform.InputConstants;
import dev.totaltax.particle.event.EventTarget;
import dev.totaltax.particle.event.impl.EventSendPacket;
import dev.totaltax.particle.module.Category;
import dev.totaltax.particle.module.Module;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

public class NoFall extends Module {
    public NoFall() {
        super("No Fall", InputConstants.KEY_N, Category.PLAYER);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        Packet<?> p = event.getPacket();

        if (p instanceof ServerboundMovePlayerPacket) {
            ServerboundMovePlayerPacket movePlayerPacket = (ServerboundMovePlayerPacket) p;

            movePlayerPacket.onGround = true;
        }
    }
}
