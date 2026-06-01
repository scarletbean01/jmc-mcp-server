package io.github.deplague.jmcmcp.domain.model;

/**
 * Container configuration metrics for system health analysis.
 */
public record SystemContainerConfig(
        String cpuShares,
        String cpuPeriod,
        String cpuQuota,
        String memoryLimit,
        String swapLimit
) {
}
