package dev.totaltax.graviton.ui;

import com.labymedia.ultralight.UltralightLoadException;
import dev.totaltax.graviton.Constants;
import dev.totaltax.graviton.Graviton;
import net.minecraft.client.Minecraft;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.Objects;

public class ResourceManager {

    private static final File clientDir = new File(Minecraft.getInstance().gameDirectory, Graviton.getInstance().getName());
    public static final File ultralightDir = new File(clientDir, "ultralight");
    private static final File tempDir = new File(clientDir, "temp");
    private static File binDir = null;
    private static final File resourceDir = new File(ultralightDir, "resources");

    public static void loadUltralight() throws URISyntaxException, UltralightLoadException, IOException {
        if (Constants.system.IS_WINDOWS) {
            binDir = new File(ultralightDir, "/bin/windows");
        } else if (Constants.system.IS_UNIX) {
            binDir = new File(ultralightDir, "/bin/linux");
        }

    }
}
