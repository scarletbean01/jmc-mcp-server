package io.github.deplague.jmcmcp.domain.model;

/**
 * Per-host:port socket connection statistics.
 */
public record NetworkConnectEntry(
        String hostPort,
        long count,
        String avgDuration,
        String maxDuration
) {
}
