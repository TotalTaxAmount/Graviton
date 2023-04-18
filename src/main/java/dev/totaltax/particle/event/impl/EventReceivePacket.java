package dev.totaltax.particle.event.impl;

import dev.totaltax.particle.event.Event;
import net.minecraft.network.protocol.Packet;

public class EventReceivePacket extends Event {
    Packet<?> packet;

    public EventReceivePacket(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return this.packet;
    }

    public void setPacket(Packet<?> p) {
        this.packet = p;
    }
}
