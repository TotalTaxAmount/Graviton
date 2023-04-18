package dev.totaltax.graviton.module.impl.player;

import com.mojang.blaze3d.platform.InputConstants;
import dev.totaltax.graviton.event.EventTarget;
import dev.totaltax.graviton.event.impl.EventSendPacket;
import dev.totaltax.graviton.module.Category;
import dev.totaltax.graviton.module.Module;
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
