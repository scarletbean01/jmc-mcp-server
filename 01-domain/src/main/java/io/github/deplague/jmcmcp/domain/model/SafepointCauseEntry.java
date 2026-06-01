package io.github.deplague.jmcmcp.domain.model;

/**
 * Safepoint cause distribution entry.
 */
public record SafepointCauseEntry(
        String cause,
        long count,
        long totalNanos,
        long avgNanos,
        long maxNanos
) {
}
