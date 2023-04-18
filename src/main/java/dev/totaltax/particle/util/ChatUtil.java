package dev.totaltax.particle.util;


import dev.totaltax.particle.Particle;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class ChatUtil {

    private static Minecraft mc = Minecraft.getInstance();
    public static void sendChat(String s) {
        mc.gui.getChat().addMessage(Component.literal("[" + ChatFormatting.BLUE + Particle.getInstance().getName() + ChatFormatting.RESET + "]" + s));
    }



    public static void sendFormattedChat(String s, Type type) {
        switch (type) {
            case INFO -> {
                sendChat("[" + ChatFormatting.BLUE + "INFO" + ChatFormatting.RESET + "] " + s);
            } case DEBUG -> {
                sendChat("[" + ChatFormatting.YELLOW + "DEBUG" + ChatFormatting.RESET + "] " + s);
            } case ERROR ->  {
                sendChat("[" + ChatFormatting.RED + "ERROR" + ChatFormatting.RESET + "] " + s);
            } default -> {
                sendChat(s);
            }
        }
    }

    public static void sendInfo(String s) {
        sendFormattedChat(s, Type.INFO);
    }
    public static void sendDebug(String s) {
        sendFormattedChat(s, Type.DEBUG);
    }
    public static void sendError(String s) {
        sendFormattedChat(s, Type.ERROR);
    }

    public enum Type {
        INFO, DEBUG, ERROR
    }
}
