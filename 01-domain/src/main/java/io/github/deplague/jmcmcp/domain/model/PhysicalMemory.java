package io.github.deplague.jmcmcp.domain.model;

/**
 * Physical memory metrics.
 */
public record PhysicalMemory(
        String totalSize,
        String minUsed,
        String maxUsed,
        String avgUsed
) {
}
