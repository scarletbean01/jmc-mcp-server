package io.github.deplague.jmcmcp.domain.model;

/**
 * Thread sleep hotspot with aggregated duration and occurrence count.
 */
public record SleepHotspot(
        String totalSleepTime,
        long count,
        String trace
) {
}
