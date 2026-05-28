package io.github.deplague.jmcmcp.domain.model;

/**
 * Time-To-Safepoint summary.
 */
public record TtspSummary(
        long avgNanos,
        long maxNanos,
        long p95Nanos
) {
}
