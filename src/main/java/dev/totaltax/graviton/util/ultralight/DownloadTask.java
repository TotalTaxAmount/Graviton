package dev.totaltax.graviton.util.ultralight;

import dev.totaltax.graviton.Graviton;
import dev.totaltax.graviton.util.HTTPDownload;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DownloadTask extends SwingWorker<Void, Void> {

    private final String url;
    private final String saveDir;
    private final UltralightDownloadGUI gui;

    public DownloadTask(String nativeUrl, UltralightDownloadGUI gui, String saveDir) {
        this.url = nativeUrl;
        this.saveDir = saveDir;
        this.gui = gui;
    }
    @Override
    protected Void doInBackground() throws Exception {
        try {
            HTTPDownload download = new HTTPDownload();
            download.downloadFile(url);

            String savePath = saveDir + File.separator + "ultralight.7z";
            if (!new File(saveDir).exists())
                new File(saveDir).mkdirs();

            InputStream inputStream = download.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(savePath);

            byte[] buffer = new byte[4096];
            int read = -1;
            long totalRead = 0;
            int completed = 0;
            long size = download.getContentLength();

            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
                totalRead += read;
                completed = (int) (totalRead * 100 / size);

                setProgress(completed);
            }

            outputStream.close();
            download.disconnect();;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(gui, "Error downloading file: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            setProgress(0);
            cancel(true);
        }
        return null;
    }

    @Override
    protected void done() {
        if (!isCancelled()) {
            Graviton.getInstance().getLogger().info("Done downloading ultralight");
            gui.dispose();
        }
    }
}
