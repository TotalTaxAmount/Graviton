package dev.totaltax.bootstrapper.ultralight;

import dev.totaltax.graviton.Constants;
import dev.totaltax.graviton.Graviton;
import dev.totaltax.graviton.util.FileUtils;
import dev.totaltax.graviton.util.download.DownloadGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.vehicle.Minecart;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class Ultralight {
    public static void getNatives() throws InterruptedException {
        System.setProperty("java.awt.headless", "false");
        DownloadGUI gui = new DownloadGUI();
        File dir = new File(FileUtils.createPath(System.getProperty("user.dir"), Graviton.getInstance().getName(), "temp").toURI());
        if (!dir.exists()) dir.mkdirs();

        SwingUtilities.invokeLater(() -> {
            gui.setVisible(true);
            gui.setModal(true);
            gui.start(Objects.requireNonNull(getUrl()), FileUtils.createPath(dir.toString(), "ultralightsdk.7z").toString());
        });
        AtomicBoolean closed = new AtomicBoolean(false);
        gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        gui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                synchronized(closed) {
                    closed.set(true);
                    closed.notify();
                }
                super.windowClosed(e);
            }
        } );

        gui.setVisible(true);
        synchronized(closed) {
            while (!closed.get()) {
                closed.wait();
            }
        }

    }

    public static int getOS() {
        if (Constants.system.IS_WINDOWS) {
            return  0;
        } else if (Constants.system.IS_UNIX || Constants.system.IS_MAC) {
            return 1;
        } else {
            return 2;
        }
    }

    private static String getUrl() {
        switch (getOS()) {
            case 0 -> {
                return "https://github.com/ultralight-ux/Ultralight/releases/download/v1.2.1/ultralight-sdk-1.2.1-win-x64.7z";
            } case 1 -> {
                return "https://github.com/ultralight-ux/Ultralight/releases/download/v1.2.1/ultralight-sdk-1.2.1-linux-x64.7z";
            } case 2 -> {
                Graviton.getInstance().getLogger().error("Could not get OS");
                return null;
            }default -> {
                return "";
            }
        }
    }
}
