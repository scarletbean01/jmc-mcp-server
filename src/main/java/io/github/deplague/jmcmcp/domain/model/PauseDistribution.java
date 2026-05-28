package io.github.deplague.jmcmcp.domain.model;

/**
 * GC pause distribution metrics.
 */
public record PauseDistribution(
        long count,
        String avg,
        String p50,
        String p95,
        String p99,
        String max
) {
}
