package dev.totaltax.graviton.ui;

import com.labymedia.ultralight.UltralightLoadException;
import dev.totaltax.graviton.Constants;
import dev.totaltax.graviton.Graviton;
import dev.totaltax.graviton.util.ultralight.UltralightDownloadGUI;
import net.minecraft.client.Minecraft;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
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

        if (!binDir.exists()) {
            getNatives();
        }
    }

    private static void getNatives() {
        String url;
        if (Constants.system.IS_WINDOWS) {
            url = "";
        } else if (Constants.system.IS_UNIX) {
            url = "https://github.com/ultralight-ux/Ultralight/releases/download/v1.2.1/ultralight-sdk-1.2.1-linux-x64.7z";
        } else if (Constants.system.IS_MAC){
            url = "";
        } else {
            url = "";
        }

        System.setProperty("java.awt.headless", "false");
        SwingUtilities.invokeLater(() -> {
            UltralightDownloadGUI download = new UltralightDownloadGUI();
            download.setVisible(true);
            download.download(url, tempDir.getPath() + "/download");
        });
    }



    private static void copyFolder(Path source, Path target, CopyOption... options)
            throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                Files.createDirectories(target.resolve(source.relativize(dir).toString()));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                Files.copy(file, target.resolve(source.relativize(file).toString()), options);
                return FileVisitResult.CONTINUE;
            }
        });
    }

}
