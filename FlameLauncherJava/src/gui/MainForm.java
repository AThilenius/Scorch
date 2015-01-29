package gui;

import launcher.*;
import platform.CurrentPlatform;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Alec on 1/29/15.
 */
public class MainForm {
    private JFrame m_frame;
    private JPanel panel1;
    private JLabel m_statusLabel;
    private JTextArea m_descriptionLabel;
    private JProgressBar m_progressBar;
    private JButton m_launchButton;

    private Color m_errorColor = new Color(153, 0, 0);
    private JavaFinder m_javaFinder = new JavaFinder();

    public MainForm(JFrame frame) {
        m_frame = frame;
        m_statusLabel.setText("Polling System Properties");
        m_progressBar.setIndeterminate(true);
        m_descriptionLabel.setVisible(false);
        m_launchButton.setVisible(false);
        m_descriptionLabel.addComponentListener(new ComponentAdapter() {
        });
    }

    public void beginLaunchCycle() {
        LaunchCommandBuilder builder = new LaunchCommandBuilder();
        scanSystem();
        scanFiles(builder);
        extractNatives(builder);

        // Also launches if all conditions are met
        preLaunchCheck(builder);
    }

    private void scanSystem() {
        // Scan any other system dependencies
    }

    private void scanFiles(LaunchCommandBuilder builder) {
        m_statusLabel.setText("Scanning Files");
        java.util.List<HttpFileDownloadTask> resourceTasks = ResourceParser.getResourceTasks();

        if (resourceTasks.size() > 0) {
            m_statusLabel.setText("Downloading " + resourceTasks.size() + " missing files.");
            m_progressBar.setIndeterminate(false);
            HttpTaskRunner runner = new HttpTaskRunner(resourceTasks, m_progressBar);
            runner.run();
            m_progressBar.setIndeterminate(true);
        }
    }

    private void extractNatives(LaunchCommandBuilder builder) {
        m_statusLabel.setText("Extracting Natives");
        m_progressBar.setIndeterminate(false);
        NativesExtractor nativesExtractor = new NativesExtractor(m_progressBar);
        nativesExtractor.extractAll(ResourceParser.getNativesPaths(), builder.getNativesPath());
    }

    private void preLaunchCheck(final LaunchCommandBuilder builder) {
        // Check all conditions, short-circuit if they are met
        if (m_javaFinder.hasCorrectVersion()) {
            launchMinecraft(builder);
        } else {
            m_frame.setMinimumSize(new Dimension(500, 200));
            m_frame.setMaximumSize(new Dimension(500, 200));
            m_launchButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    launchMinecraft(builder);
                }
            });
            m_statusLabel.setText("Failed to find Java 1.6!");
            m_descriptionLabel.setText("You do not have Java 1.6.X installed! You can try launching anyway but " +
                    "you will not get help with crashes. Download Java 1.6 from:\n" +
                    m_javaFinder.getDownloadLink());
            m_descriptionLabel.setVisible(true);
            m_statusLabel.setForeground(m_errorColor);
            m_progressBar.setVisible(false);
            m_launchButton.setVisible(true);
            m_launchButton.setText("Launch Anyway!");
        }
    }

    private void launchMinecraft(LaunchCommandBuilder builder) {
        m_statusLabel.setText("Launching Minecraft");
        m_progressBar.setIndeterminate(false);
        m_progressBar.setValue(100);

        // Launch!
        Process proc = null;

        try {
            String fullCommand = builder.getFullCommand();
            System.out.println(fullCommand);
            switch(CurrentPlatform.getType()) {
                case OSX:
                    proc = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", fullCommand},
                            null, new File(builder.getRootPath()));
                    break;
                case WINDOWS:
                    proc = Runtime.getRuntime().exec(fullCommand, null, new File(builder.getRootPath()));
                    break;
                case LINUX:
                    proc = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", fullCommand},
                            null, new File(builder.getRootPath()));
                    break;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if (proc != null) {
            System.out.println("Launching Minecraft...");
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader error = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
                while ((line = error.readLine()) != null) {
                    System.err.println(line);
                }
                proc.waitFor();
                in.close();
                proc.destroy();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Failed to start child process.");
            m_statusLabel.setForeground(m_errorColor);
            m_statusLabel.setText("Failed to start child process.");
        }

        System.exit(0);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flame");
        MainForm form = new MainForm(frame);
        frame.setContentPane(form.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set Frame Size
        frame.setMinimumSize(new Dimension(500, 100));
        frame.setMaximumSize(new Dimension(500, 100));
        frame.setResizable(false);

        // Center Frame
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

        // Start it up
        frame.pack();
        frame.setVisible(true);

        form.beginLaunchCycle();
    }
}
