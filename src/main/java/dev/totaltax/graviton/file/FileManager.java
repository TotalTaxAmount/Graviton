package dev.totaltax.graviton.file;

import dev.totaltax.graviton.Graviton;
import net.minecraft.client.Minecraft;

import java.io.File;

public class FileManager {

    File dir = new File(Minecraft.getInstance().gameDirectory, Graviton.getInstance().getName());

    File temp = new File(dir, "/temp");

    public void init() {
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();
        }
    }

    public File getDir() {
        return dir;
    }
}
