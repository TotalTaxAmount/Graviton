package dev.totaltax.graviton.util.download;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

public class DownloadGUI extends JDialog implements PropertyChangeListener {

    private JLabel textLabel = new JLabel("Downloading ultralight natives...");
    private JLabel progressLabel = new JLabel("Progress: ");
    private JProgressBar progressBar = new JProgressBar(0, 100);

    public DownloadGUI() {
        super();

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);

        progressBar.setPreferredSize(new Dimension(200, 20));
        progressBar.setStringPainted(true);

        constraints.gridx = 0;
        constraints.gridy = 0;
        add(textLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.WEST;
        add(progressLabel, constraints);

        constraints.gridx = 1;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(progressBar, constraints);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void start(String url, String save) {
        if (url.equals("")) {
//            Graviton.getInstance().getLogger().error("No url provided");
            return;
        }

        try {
            progressBar.setValue(0);

            DownloadTask task = new DownloadTask(this, url, save);
            task.addPropertyChangeListener(this);
            task.execute();
        } catch (Exception e) {
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
