package io.github.deplague.jmcmcp.domain.model;

/**
 * Single contention entry.
 */
public record ContentionEntry(
        String totalDuration,
        String monitorClass,
        String stackTrace
) {
}
