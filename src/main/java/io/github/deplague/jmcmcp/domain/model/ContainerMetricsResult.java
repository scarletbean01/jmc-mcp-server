package io.github.deplague.jmcmcp.domain.model;

import java.util.Optional;

/**
 * Result of container metrics analysis.
 */
public record ContainerMetricsResult(
        Optional<ContainerConfig> config,
        Optional<ContainerCpuUsage> cpuUsage,
        Optional<ContainerMemoryUsage> memoryUsage
) {

    public boolean hasData() {
        return config.isPresent() || cpuUsage.isPresent() || memoryUsage.isPresent();
    }
}
