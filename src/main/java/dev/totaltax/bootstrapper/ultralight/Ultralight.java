package dev.totaltax.bootstrapper.ultralight;

import dev.totaltax.graviton.Constants;
import dev.totaltax.graviton.Graviton;
import dev.totaltax.graviton.util.FileUtils;
import dev.totaltax.graviton.util.download.DownloadGUI;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class Ultralight {

    private static String NAME = Graviton.getInstance().getName();
    private static String WORKINGDIR = System.getProperty("user.dir");
    public static void init() throws IOException, InterruptedException {
        if (!FileUtils.createPath(WORKINGDIR, NAME, "natives").exists()) {
            getNatives();
        }
    }


    public static void getNatives() throws InterruptedException, IOException {
        System.setProperty("java.awt.headless", "false");
        DownloadGUI gui = new DownloadGUI();
        File downloadDir = FileUtils.createPath(WORKINGDIR, NAME, "temp");
        File binDir = FileUtils.createPath(WORKINGDIR, NAME, "natives", "bin", "resources");
        String path = FileUtils.createPath(downloadDir.toString(), "ultralightsdk.7z").toString();
        if (!downloadDir.exists()) downloadDir.mkdirs();
        if (!binDir.exists()) binDir.mkdirs();

        SwingUtilities.invokeLater(() -> {
            gui.setVisible(true);
            gui.setModal(true);
            gui.start(Objects.requireNonNull(getUrl()), path);
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

        SevenZFile compressed = new SevenZFile(new File(path));
        SevenZArchiveEntry entry;
        File natives = FileUtils.createPath(WORKINGDIR, NAME, "natives");
        natives.mkdirs();
        while ((entry = compressed.getNextEntry()) != null) {
            if (entry.getName().contains("bin") && !entry.getName().equals("bin") && !entry.getName().equals("bin/resources") && !entry.getName().contains("inspector")) {
                FileOutputStream out = new FileOutputStream(natives + File.separator + entry.getName());
                byte[] content = new byte[(int) entry.getSize()];
                compressed.read(content, 0, content.length);
                out.write(content);
                out.close();
            }
        }
        compressed.close();
        org.apache.commons.io.FileUtils.deleteDirectory(downloadDir);
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
                throw new RuntimeException("Could not get os");
            }default -> {
                return "";
            }
        }
    }
}
