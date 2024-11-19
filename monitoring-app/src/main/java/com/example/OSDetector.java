package com.example;

public class OSDetector {
    public static MonitorInterface getMonitor() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return new WindowsMonitor();
        } else if (osName.contains("mac")) {
            return new MacOSMonitor();
        } else if (osName.contains("nix") || osName.contains("nux")) {
            return new LinuxMonitor();
        } else {
            throw new UnsupportedOperationException("Unsupported OS: " + osName);
        }
    }
}
