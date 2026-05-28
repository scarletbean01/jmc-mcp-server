package io.github.deplague.jmcmcp.domain.model;

/**
 * CPU load metrics.
 */
public record CpuLoad(
        String avgMachineTotal,
        String maxMachineTotal,
        String avgJvmUser,
        String avgJvmSystem
) {
}
