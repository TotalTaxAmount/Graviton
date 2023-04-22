package dev.totaltax.graviton.module.impl.combat;

import com.mojang.blaze3d.platform.InputConstants;
import dev.totaltax.graviton.event.EventTarget;
import dev.totaltax.graviton.event.impl.EventReceivePacket;
import dev.totaltax.graviton.module.Category;
import dev.totaltax.graviton.module.Module;
import dev.totaltax.graviton.util.ChatUtil;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;

public class Velocity extends Module {

    public Velocity() {
        super("Velocity", InputConstants.KEY_V, Category.COMBAT);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event){
        if (event.getPacket() instanceof ClientboundSetEntityMotionPacket) {
            ClientboundSetEntityMotionPacket packet = (ClientboundSetEntityMotionPacket) event.getPacket();
            if (packet.id == mc.player.getId()){
                packet.xa = 0;
                packet.ya = 0;
                packet.za = 0;
            }
        }
    }
}
