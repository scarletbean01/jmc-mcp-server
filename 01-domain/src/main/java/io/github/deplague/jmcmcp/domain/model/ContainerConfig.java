package io.github.deplague.jmcmcp.domain.model;

import java.util.Optional;

/**
 * Container configuration snapshot.
 */
public record ContainerConfig(
        Optional<String> cpuShares,
        Optional<String> cpuPeriod,
        Optional<String> cpuQuota,
        Optional<String> memoryLimit,
        Optional<String> swapLimit,
        Optional<String> memorySoftLimit
) {
}
