package io.github.deplague.jmcmcp.domain.model;

/**
 * Error entry.
 */
public record ErrorEntry(
        String className,
        String message,
        String stackTrace,
        long count,
        String severity
) {
}
