package io.github.deplague.jmcmcp.domain.model;

/**
 * CPU load metrics from JFR CPULoad events.
 */
public record CpuLoadSummary(double avgJvmUser, double avgJvmSystem, double avgMachineTotal,
                             double efficiency, int sampleCount) {
}
