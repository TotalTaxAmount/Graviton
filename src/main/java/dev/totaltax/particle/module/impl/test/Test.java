package dev.totaltax.particle.module.impl.test;

import com.mojang.blaze3d.platform.InputConstants;
import dev.totaltax.particle.event.EventTarget;
import dev.totaltax.particle.event.impl.EventPreMotion;
import dev.totaltax.particle.event.impl.EventReceivePacket;
import dev.totaltax.particle.event.impl.EventSendPacket;
import dev.totaltax.particle.module.Category;
import dev.totaltax.particle.module.Module;
import dev.totaltax.particle.util.ChatUtil;

public class Test extends Module {
    public Test() {
        super("Test", InputConstants.KEY_M, Category.NONE);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        //ChatUtil.sendDebug("Send packet: " + event.getPacket());
        //System.out.println("Send packet: " + event.getPacket());
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        //ChatUtil.sendDebug("Receive packet: " + event.getPacket());
        //System.out.println("Receive packet: " + event.getPacket());
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        ChatUtil.sendChat("------------------------");
        ChatUtil.sendDebug("X: " + event.getX());
        ChatUtil.sendDebug("Y: " + event.getY());
        ChatUtil.sendDebug("Z: " + event.getZ());
        ChatUtil.sendDebug("Yaw: " + event.getYaw());
        ChatUtil.sendDebug("Pitch: " + event.getPitch());
        ChatUtil.sendDebug("Ground: " + event.isOnGround());



    }
}
