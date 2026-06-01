package io.github.deplague.jmcmcp.domain.model;

/**
 * Represents a lock contention site with monitor class, top stack frame, and aggregated metrics.
 */
public record CorrelateLockSite(String monitorClass, String topFrame, long totalDurationMs, long count) {
}
