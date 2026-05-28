package io.github.deplague.jmcmcp.domain.model;

/**
 * Per-host:port socket read statistics.
 */
public record NetworkReadEntry(
        String hostPort,
        long count,
        String totalBytes,
        String avgDuration
) {
}
