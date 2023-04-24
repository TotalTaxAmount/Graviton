package dev.totaltax.bootstrapper.ultralight;

import com.labymedia.ultralight.UltralightJava;
import com.labymedia.ultralight.UltralightLoadException;
import com.labymedia.ultralight.gpu.UltralightGPUDriverNativeUtil;
import dev.totaltax.graviton.Constants;
import dev.totaltax.graviton.Graviton;
import dev.totaltax.graviton.util.FileUtils;
import dev.totaltax.graviton.util.download.DownloadGUI;
import org.apache.commons.compress.archivers.sevenz.*;
import org.tukaani.xz.LZMA2InputStream;
import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.LZMAInputStream;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class Ultralight {

    private static String NAME = Graviton.getInstance().getName();
    private static String WORKINGDIR = System.getProperty("user.dir");

    public static void init() throws IOException, InterruptedException, UltralightLoadException {
        File binDir = FileUtils.createPath(WORKINGDIR, NAME, "natives", "bin");
        if (!FileUtils.createPath(WORKINGDIR, NAME, "natives").exists()) {
            getNatives();
        }

        UltralightJava.extractNativeLibrary(binDir.toPath());
        UltralightGPUDriverNativeUtil.extractNativeLibrary(binDir.toPath());

//        Stream.of(Objects.requireNonNull(binDir.listFiles())).forEach((file -> {
//            if (!file.getName().endsWith(".dll")) return;
//            Path f = new File(binDir.toPath().toFile(), file.getName()).toPath().toAbsolutePath();
//            System.load(f.toAbsolutePath().toString());
//        }));

        System.load(new File(binDir.toPath().toFile(), "UltralightCore.dll").toPath().toAbsolutePath().toString());
        System.load(new File(binDir.toPath().toFile(), "glib-2.0-0.dll").toPath().toAbsolutePath().toString());
        System.load(new File(binDir.toPath().toFile(), "gobject-2.0-0.dll").toPath().toAbsolutePath().toString());
        System.load(new File(binDir.toPath().toFile(), "gmodule-2.0-0.dll").toPath().toAbsolutePath().toString());
        System.load(new File(binDir.toPath().toFile(), "gio-2.0-0.dll").toPath().toAbsolutePath().toString());
        System.load(new File(binDir.toPath().toFile(), "gstreamer-full-1.0.dll").toPath().toAbsolutePath().toString());
        System.load(new File(binDir.toPath().toFile(), "WebCore.dll").toPath().toAbsolutePath().toString());
        System.load(new File(binDir.toPath().toFile(), "Ultralight.dll").toPath().toAbsolutePath().toString());
        System.load(new File(binDir.toPath().toFile(), "ultralight-java-gpu.dll").toPath().toAbsolutePath().toString());
        System.load(new File(binDir.toPath().toFile(), "AppCore.dll").toPath().toAbsolutePath().toString());
        System.load(new File(binDir.toPath().toFile(), "ultralight-java.dll").toPath().toAbsolutePath().toString());

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
            if (entry.getName().startsWith("bin") && !entry.getName().equals("bin") && !entry.getName().equals("bin/resources") && !entry.getName().contains("inspector")) {
                entry.setContentMethods(new SevenZMethodConfiguration(SevenZMethod.LZMA, new LZMA2Options(5)));
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
                return "https://ultralight-sdk.sfo2.cdn.digitaloceanspaces.com/ultralight-sdk-latest-win-x64.7z";
            } case 1 -> {
                return "https://ultralight-sdk.sfo2.cdn.digitaloceanspaces.com/ultralight-sdk-latest-linux-x64.7z";
            } case 2 -> {
                throw new RuntimeException("Could not get os");
            }default -> {
                return "";
            }
        }
    }
}
