package com.example;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

public class MacOSMonitor implements MonitorInterface {
    private final SystemInfo systemInfo;
    private final CentralProcessor processor;
    private final GlobalMemory memory;
    private long[] prevCpuTicks; // Store previous CPU ticks

    public MacOSMonitor() {
        systemInfo = new SystemInfo();
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        processor = hal.getProcessor();
        memory = hal.getMemory();
        prevCpuTicks = processor.getSystemCpuLoadTicks(); // Initialize with current ticks
    }

    @Override
    public double getCpuLoad() {
        long[] currentCpuTicks = processor.getSystemCpuLoadTicks(); // Get current CPU ticks
        double load = processor.getSystemCpuLoadBetweenTicks(prevCpuTicks) * 100; // Calculate load as a percentage
        prevCpuTicks = currentCpuTicks; // Update previous ticks for the next calculation
        return load; // Return as a percentage
    }

    @Override
    public double getFreeMemory() {
        double freeRamSize = memory.getAvailable(); // Free RAM in bytes
        return freeRamSize / (1024 * 1024 * 1024); // Convert to GiB
    }

    @Override
    public double getTotalMemory() {
        double totalRamSize = memory.getTotal(); // Total RAM in bytes
        return totalRamSize / (1024 * 1024 * 1024); // Convert to GiB
    }
}
