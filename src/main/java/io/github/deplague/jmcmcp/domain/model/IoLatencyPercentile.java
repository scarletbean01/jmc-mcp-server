package io.github.deplague.jmcmcp.domain.model;

/**
 * I/O latency percentile entry.
 */
public record IoLatencyPercentile(
        String operation,
        String p50,
        String p95,
        String p99,
        String max
) {
}
