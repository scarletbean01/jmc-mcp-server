package io.github.deplague.jmcmcp.domain.model;

/**
 * A single exception entry.
 */
public record ExceptionEntry(String className, String message, String stackTrace, long count) {
}
