package io.github.deplague.jmcmcp.domain.model;

/**
 * Park site entry.
 */
public record ParkSiteEntry(
        String stackTrace,
        long count,
        String avgDuration,
        String maxDuration
) {
}
