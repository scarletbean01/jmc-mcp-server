package io.github.deplague.jmcmcp.domain.model;

/**
 * A blocked stack trace occurrence for a lock holder issue.
 */
public record BlockedTraceEntry(
        String stackTrace,
        long count
) {
}
