package io.github.deplague.jmcmcp.domain.model;

/**
 * Represents an I/O hotspot with endpoint, top stack frame, and aggregated metrics.
 */
public record CorrelateIoSite(String eventType, String endpoint, String topFrame, long totalDurationMs, long count, long totalBytes) {
}
