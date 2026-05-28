package io.github.deplague.jmcmcp.domain.model;

/**
 * GC pause time metrics.
 */
public record GcPauseTimes(
        String avgPause,
        String maxPause,
        String totalPause
) {
}
