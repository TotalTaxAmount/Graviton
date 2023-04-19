package dev.totaltax.graviton.util;

import dev.totaltax.graviton.Graviton;
import net.minecraft.client.gui.screens.EditServerScreen;
import net.minecraft.client.particle.Particle;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPDownload {
    private HttpURLConnection connection;

    private InputStream inputStream;

    private String fName;
    private int contentLength;

    public void downloadFile(String fileURL) throws IOException {
        URL url = new URL(fileURL);
        connection = (HttpURLConnection) url.openConnection();
        int res = connection.getResponseCode();

        System.out.println("url: " + connection);

        if (res == HttpURLConnection.HTTP_OK) {
            System.out.println("George kill your self");
            String diposition = connection.getHeaderField("Content-Disposition");
            String type = connection.getContentType();

            if (diposition != null) {
                int index = diposition.indexOf("filename=");
                if (index > 0) {
                    fName = diposition.substring(index + 10, diposition.length() - 1);
                }
            } else {
                fName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
            }

            Graviton.getInstance().getLogger().debug("Content-Type: " + type);
            Graviton.getInstance().getLogger().debug("Content-Disposition: " + diposition);
            Graviton.getInstance().getLogger().debug("Content-Length: " + contentLength);
            Graviton.getInstance().getLogger().debug("File name: " + fName);

            inputStream = connection.getInputStream();
        } else {
            throw new IOException(String.format("No file to download. (Res Code: {})", res));
        }
    }

    public void disconnect() throws IOException {
        inputStream.close();
        connection.disconnect();
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getfName() {
        return fName;
    }

    public int getContentLength() {
        return contentLength;
    }
}
