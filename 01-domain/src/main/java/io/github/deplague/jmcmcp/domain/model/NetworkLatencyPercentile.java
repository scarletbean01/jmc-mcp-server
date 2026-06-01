package io.github.deplague.jmcmcp.domain.model;

/**
 * Network operation latency percentile breakdown.
 */
public record NetworkLatencyPercentile(
        String operation,
        String p50,
        String p95,
        String p99,
        String max
) {
}
