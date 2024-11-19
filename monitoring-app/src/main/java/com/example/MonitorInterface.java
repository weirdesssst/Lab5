package com.example;

public interface MonitorInterface {
    double getCpuLoad();     // Returns CPU load as a percentage
    double getFreeMemory();  // Returns available memory in GiB
    double getTotalMemory(); // Returns total memory in GiB
}
