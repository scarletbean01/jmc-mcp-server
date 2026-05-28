package io.github.deplague.jmcmcp.domain.model;

/**
 * Top longest safepoint entry.
 */
public record TopSafepointEntry(
        long durationNanos,
        String cause,
        String startTime
) {
}
