package dev.totaltax.graviton.util.download;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPDownload {
    private HttpURLConnection connection;

    private InputStream inputStream;

    private String fileName;
    private int contentLength;

    public void downloadFile(String url) throws IOException {
//        URL url = new URL(url);
        connection = (HttpURLConnection) new URL(url).openConnection();
        int res = connection.getResponseCode();

        if (res == HttpURLConnection.HTTP_OK) {
            String diposition = connection.getHeaderField("Content-Disposition");
            String type = connection.getContentType();
            contentLength = connection.getContentLength();

            if (diposition != null) {
                int index = diposition.indexOf("filename=");
                if (index > 0) {
                    fileName = diposition.substring(index + 10, diposition.length() - 1);
                }
            } else {
                fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
            }

//            Graviton.getInstance().getLogger().debug("Content-Type: " + type);
//            Graviton.getInstance().getLogger().debug("Content-Disposition: " + diposition);
//            Graviton.getInstance().getLogger().debug("Content-Length: " + contentLength);
//            Graviton.getInstance().getLogger().debug("File name: " + fName);

            inputStream = connection.getInputStream();
        } else {
            throw new IOException(String.format("No file to download. (Res Code: {})", res));
        }
    }

    public void downloadFile(String url, String savePath) throws IOException {
        HTTPDownload download = new HTTPDownload();
        download.downloadFile(url);

        InputStream inputStream1 = download.getInputStream();
        FileOutputStream outputStream = new FileOutputStream(savePath);

        byte[] buffer = new byte[4096];
        int bytesRead = -1;

        while ((bytesRead = inputStream1.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.close();
    }

    public void disconnect() throws IOException {
        inputStream.close();
        connection.disconnect();
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getFileName() {
        return fileName;
    }

    public int getContentLength() {
        return contentLength;
    }
}
