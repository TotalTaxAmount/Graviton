package dev.totaltax.graviton.file;

import dev.totaltax.graviton.Graviton;
import net.minecraft.client.Minecraft;

import java.io.File;

public class FileManager {

    File dir = new File(System.getProperty("user.dir"), Graviton.getInstance().getName());

    File ultralight = new File(dir, "/ultralight");

    public void init() {
        if (ultralight.mkdirs()) Graviton.getInstance().getLogger().info("Created ultralight dir");
    }

    public File getDir() {
        return dir;
    }
}
