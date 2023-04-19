package dev.totaltax.graviton.util.ultralight;

import dev.totaltax.graviton.Graviton;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class UltralightDownloadGUI extends JFrame implements PropertyChangeListener {

    private JLabel info = new JLabel("Downloading Ultralight");

    private JLabel labelProgress = new JLabel("Download");
    private JProgressBar progressBar = new JProgressBar(0, 100);

    public UltralightDownloadGUI() {
        super("Downloading ultralight");

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);

        progressBar.setPreferredSize(new Dimension(200, 10));
        progressBar.setStringPainted(true);

        // Adding shit
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(info, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 1;
        add(labelProgress, constraints);

        constraints.gridx = 1;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(progressBar, constraints);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    }
    public void download(String url, String save) {
        if (url.equals("")) {
            Graviton.getInstance().getLogger().error("No url provided");
            return;
        }

        try {
            progressBar.setValue(0);

            DownloadTask task = new DownloadTask(url, this, save);
            task.addPropertyChangeListener(this);
            task.execute();
        } catch (Exception e) {
            Graviton.getInstance().getLogger().error("Failed to download: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (propertyChangeEvent.getPropertyName().equals("progress")) {
            int progress = (Integer) propertyChangeEvent.getNewValue();
            progressBar.setValue(progress);
        }
    }
}
