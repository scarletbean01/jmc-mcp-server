package io.github.deplague.jmcmcp.domain.model;

import java.util.Optional;

/**
 * Result of system health analysis.
 */
public record SystemHealthResult(
        Optional<CpuLoad> cpuLoad,
        Optional<PhysicalMemory> physicalMemory,
        Optional<CpuInfo> cpuInfo,
        Optional<SystemContainerConfig> containerConfig,
        boolean hasData,
        boolean highCpuDetected
) {
}
