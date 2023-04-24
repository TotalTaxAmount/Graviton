package dev.totaltax.bootstrapper;

import dev.totaltax.bootstrapper.ultralight.Ultralight;
import net.minecraft.client.main.Main;
import net.minecraft.network.Connection;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class Bootstrapper {
    public static void main(String[] args)
    {
        try {
            Ultralight.init();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }

        String assets = System.getenv().containsKey("assetDirectory") ? System.getenv("assetDirectory") : "assets";
        Main.main(concat(new String[]{"--version", "mcp", "--accessToken", "0", "--assetsDir", assets, "--assetIndex", "3", "--userProperties", "{}"}, args));
    }

    public static <T> T[] concat(T[] first, T[] second)
    {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
