package io.github.deplague.jmcmcp.domain.model;

/**
 * I/O endpoint entry.
 */
public record IoEndpointEntry(
        String target,
        String maxDuration,
        long count,
        String totalBytes,
        String stackTrace
) {
}
