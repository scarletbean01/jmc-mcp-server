package io.github.deplague.jmcmcp.domain.model;

/**
 * Represents a single allocation flame graph entry with call path and bytes.
 */
public record AllocationFlameEntry(
    String callPath,
    long bytes,
    String formattedBytes,
    double percentage
) {
}
