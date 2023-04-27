package dev.totaltax.graviton.util;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;

import java.util.Objects;

public class PacketUtils {
    public static void sendPacket(Packet<?> packet) {
        Objects.requireNonNull(Minecraft.getInstance().getConnection()).getConnection().send(packet);
    }

    public static void sendPacketNoEvent(Packet<?> packet) {
        Objects.requireNonNull(Minecraft.getInstance().getConnection()).getConnection().sendNoEvent(packet);
    }
}
