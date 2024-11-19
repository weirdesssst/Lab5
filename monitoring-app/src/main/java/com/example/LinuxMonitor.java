package com.example;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

public class LinuxMonitor implements MonitorInterface {
    private final OperatingSystemMXBean osBean;
    private static final double GiB = Math.pow(2, 30); // 1 GiB in bytes

    public LinuxMonitor() {
        osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    }

    @Override
    public double getCpuLoad() {
        return osBean.getSystemCpuLoad() * 100; // CPU load in percentage
    }

    @Override
    public double getFreeMemory() {
        double freeRamSize = (double) osBean.getFreePhysicalMemorySize(); // Free RAM in bytes
        return freeRamSize / GiB; // Convert to GiB
    }

    @Override
    public double getTotalMemory() {
        double totalRamSize = (double) osBean.getTotalPhysicalMemorySize(); // Total RAM in bytes
        return totalRamSize / GiB; // Convert to GiB
    }
}
