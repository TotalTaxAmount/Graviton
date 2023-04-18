package dev.totaltax.particle.module.impl.test;

import com.mojang.blaze3d.platform.InputConstants;
import dev.totaltax.particle.event.EventTarget;
import dev.totaltax.particle.event.impl.EventReceivePacket;
import dev.totaltax.particle.event.impl.EventSendPacket;
import dev.totaltax.particle.module.Category;
import dev.totaltax.particle.module.Module;

public class PacketTest extends Module {
    public PacketTest() {
        super("Packet Test", InputConstants.KEY_M, Category.NONE);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        System.out.println("Send packet: " + event.getPacket());
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        System.out.println("Receive packet: " + event.getPacket());
    }
}
