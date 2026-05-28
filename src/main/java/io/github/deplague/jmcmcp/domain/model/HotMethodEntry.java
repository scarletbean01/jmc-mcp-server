package io.github.deplague.jmcmcp.domain.model;

/**
 * Represents a single hot method with its aggregated sample count and formatted stack trace.
 */
public record HotMethodEntry(String stackTrace, long sampleCount) {
}
