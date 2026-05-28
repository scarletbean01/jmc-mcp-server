package io.github.deplague.jmcmcp.domain.model;

/**
 * Heap utilization metrics extracted from jdk.GCHeapSummary.
 */
public record HeapUtilization(
        String minHeapUsed,
        String avgHeapUsed,
        String maxHeapUsed,
        double heapAmplitudePct
) {
}
