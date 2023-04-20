package dev.totaltax.bootstrapper.ultralight;

import dev.totaltax.graviton.util.download.DownloadGUI;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.atomic.AtomicBoolean;

public class Ultralight {
    public static void getNatives() throws InterruptedException {
        System.setProperty("java.awt.headless", "false");
        DownloadGUI gui = new DownloadGUI();
        SwingUtilities.invokeLater(() -> {
            gui.setVisible(true);
            gui.setModal(true);
            gui.start("https://github.com/ultralight-ux/Ultralight/releases/download/v1.2.1/ultralight-sdk-1.2.1-linux-x64.7z", "/home/totaltaxamount/Documents/Projects/Intellij/Graviton/run/Graviton/ultralight");
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
}
