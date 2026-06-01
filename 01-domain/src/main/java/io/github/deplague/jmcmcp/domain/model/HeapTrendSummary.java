package io.github.deplague.jmcmcp.domain.model;

/**
 * Overall heap usage trend summary from jdk.GCHeapSummary.
 */
public record HeapTrendSummary(
        String minHeapUsed,
        String maxHeapUsed,
        String avgHeapUsed,
        String p95HeapUsed
) {
}
