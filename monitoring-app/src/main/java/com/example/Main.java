package com.example;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    private static MonitorInterface monitor;
    private static Timer timer;

    public static void main(String[] args) {
        monitor = OSDetector.getMonitor();

        JFrame frame = new JFrame("CPU and RAM MONITORING");
        frame.setSize(400, 300); // Increased height to accommodate image
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel cpuLabel = new JLabel("CPU LOAD: ");
        JLabel ramLabel = new JLabel("RAM FREE: ");
        JLabel totalRamLabel = new JLabel("TOTAL RAM: ");
        JLabel osLabel = new JLabel(); // Label for OS image

        JSlider intervalSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, 5);
        intervalSlider.setMajorTickSpacing(1);
        intervalSlider.setPaintTicks(true);
        intervalSlider.setPaintLabels(true);
        
        intervalSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int interval = intervalSlider.getValue();
                if (timer != null) {
                    timer.cancel();
                }
                if (interval == 0) {
                    startMonitoring(1000, cpuLabel, ramLabel, totalRamLabel);
                } else {
                    startMonitoring(interval * 1000, cpuLabel, ramLabel, totalRamLabel);
                }
            }
        });
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1)); // Adjusted rows to fit the OS label
        panel.add(cpuLabel);
        panel.add(ramLabel);
        panel.add(totalRamLabel);
        panel.add(osLabel); // Add OS label to the panel
        panel.add(new JLabel("Set interval for monitoring (0 = real time):"));
        panel.add(intervalSlider);
        
        frame.add(panel);
        frame.setVisible(true);

        // Load OS image
        loadOsImage(osLabel);

        // Start monitoring every 5 seconds (default)
        startMonitoring(5000, cpuLabel, ramLabel, totalRamLabel);
    }

    private static void startMonitoring(int interval, JLabel cpuLabel, JLabel ramLabel, JLabel totalRamLabel) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                double cpuLoad = monitor.getCpuLoad();
                double freeMemory = monitor.getFreeMemory();
                double totalMemory = monitor.getTotalMemory();

                SwingUtilities.invokeLater(() -> {
                    cpuLabel.setText("CPU Load: " + String.format("%.2f", cpuLoad) + " %");
                    ramLabel.setText("RAM FREE: " + String.format("%.2f", freeMemory) + " GiB");
                    totalRamLabel.setText("TOTAL RAM: " + String.format("%.2f", totalMemory) + " GiB");
                });
            }
        }, 0, interval);
    }

    private static void loadOsImage(JLabel osLabel) {
        String osName = System.getProperty("os.name").toLowerCase();
        String imagePath = "";

        if (osName.contains("win")) {
            imagePath = "resources/windows.png"; // Path to Windows image
        } else if (osName.contains("mac")) {
            imagePath = "resources/macos.png"; // Path to macOS image
        } else if (osName.contains("nix") || osName.contains("nux")) {
            imagePath = "resources/linux.png"; // Path to Linux image
        }

        // Load image and set it in the label
        try {
            ImageIcon originalIcon = new ImageIcon(Main.class.getResource("/" + imagePath)); // Load the original image
            Image originalImage = originalIcon.getImage(); // Get the image from the icon

            // Resize the image to 360x360 pixels
            Image resizedImage = originalImage.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            
            // Create a new ImageIcon from the resized image
            ImageIcon resizedIcon = new ImageIcon(resizedImage);
            
            osLabel.setIcon(resizedIcon);
            osLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the image
        } catch (Exception e) {
            e.printStackTrace();
            osLabel.setText("OS image not found!");
        }
    }
}
