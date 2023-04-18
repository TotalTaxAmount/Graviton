package dev.totaltax.particle.event.impl;

import dev.totaltax.particle.event.Event;
import net.minecraft.network.protocol.Packet;

public class EventSendPacket extends Event {
    Packet<?> packet;

    public EventSendPacket(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return this.packet;
    }

    public void setPacket(Packet<?> p) {
        this.packet = p;
    }
}