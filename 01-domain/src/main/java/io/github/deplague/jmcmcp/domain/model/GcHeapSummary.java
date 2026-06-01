package io.github.deplague.jmcmcp.domain.model;

/**
 * GC heap summary metrics.
 */
public record GcHeapSummary(
        String maxHeapUsed,
        String minHeapUsed,
        String avgHeapUsed
) {
}
