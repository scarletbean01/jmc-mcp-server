package io.github.deplague.jmcmcp.domain.model;

/**
 * Represents a single allocation hotspot with class, stack trace, and allocated bytes.
 */
public record AllocationHotspotEntry(
    String className,
    String stackTrace,
    long bytesAllocated,
    String formattedBytes
) {
}
