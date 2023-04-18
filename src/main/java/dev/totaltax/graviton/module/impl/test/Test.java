package dev.totaltax.graviton.module.impl.test;

import com.mojang.blaze3d.platform.InputConstants;
import dev.totaltax.graviton.event.EventTarget;
import dev.totaltax.graviton.event.impl.EventPreMotion;
import dev.totaltax.graviton.event.impl.EventReceivePacket;
import dev.totaltax.graviton.event.impl.EventSendPacket;
import dev.totaltax.graviton.module.Category;
import dev.totaltax.graviton.module.Module;
import dev.totaltax.graviton.util.ChatUtil;

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
