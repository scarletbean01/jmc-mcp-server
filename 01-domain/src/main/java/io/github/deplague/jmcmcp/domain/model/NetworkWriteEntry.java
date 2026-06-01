package io.github.deplague.jmcmcp.domain.model;

/**
 * Per-host:port socket write statistics.
 */
public record NetworkWriteEntry(
        String hostPort,
        long count,
        String totalBytes,
        String avgDuration
) {
}
