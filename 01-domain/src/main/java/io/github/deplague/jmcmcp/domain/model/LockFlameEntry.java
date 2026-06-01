package io.github.deplague.jmcmcp.domain.model;

/**
 * Represents a single lock flame graph entry with call path and duration.
 */
public record LockFlameEntry(
    String callPath,
    long nanos,
    String formattedDuration,
    double percentage
) {
}
