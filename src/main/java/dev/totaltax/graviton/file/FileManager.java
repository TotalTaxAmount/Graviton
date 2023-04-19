package dev.totaltax.graviton.file;

import dev.totaltax.graviton.Graviton;
import net.minecraft.client.Minecraft;

import java.io.File;

public class FileManager {

    File dir = new File(Minecraft.getInstance().gameDirectory, Graviton.getInstance().getName());
    File ultralightDir = new File(dir, "/ultralight");
    File linuxNatives = new File(ultralightDir, "/linux");
    File windowsNatives = new File(ultralightDir, "/windows");

    public void init() {
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();
            ultralightDir.mkdirs();
            linuxNatives.mkdirs();
            windowsNatives.mkdirs();
        }
    }

    public File getDir() {
        return dir;
    }
}
