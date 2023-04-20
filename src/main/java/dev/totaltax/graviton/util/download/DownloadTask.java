package dev.totaltax.graviton.util.download;

import dev.totaltax.graviton.Constants;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DownloadTask extends SwingWorker<Void, Void> {
    private final String name;
    private String downloadURL;
    private String saveDirectory;
    private DownloadGUI gui;

    public DownloadTask(DownloadGUI gui, String downloadURL, String saveDirectory) {
        this.gui = gui;
        this.downloadURL = downloadURL;
        this.saveDirectory = saveDirectory;
        this.name = Constants.system.IS_WINDOWS ? saveDirectory.substring(saveDirectory.lastIndexOf("\\") - 1) : saveDirectory.substring(saveDirectory.lastIndexOf("/"));
    }


    @Override
    protected Void doInBackground() throws Exception {
        try {
            HTTPDownload httpDownload = new HTTPDownload();
            httpDownload.downloadFile(downloadURL);

            String path = saveDirectory + File.separator + name;

            InputStream stream = httpDownload.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(path);

            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            long total = 0;
            int percent = 0;
            long size = httpDownload.getContentLength();
            while ((bytesRead = stream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                total += bytesRead;
                percent = (int) (total * 100 / size);
                setProgress(percent);
            }

            outputStream.close();
            httpDownload.disconnect();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(gui, "Error downloading file " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            setProgress(0);
            cancel(true);
        }
        return null;
    }

    @Override
    protected void done() {
        if (!isCancelled()) {
            gui.dispose();
        }
    }
}
