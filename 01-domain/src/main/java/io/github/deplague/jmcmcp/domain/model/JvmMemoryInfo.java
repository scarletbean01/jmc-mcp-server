package io.github.deplague.jmcmcp.domain.model;

/**
 * JVM memory metrics snapshot.
 */
public record JvmMemoryInfo(
        long heapUsed,
        long heapCommitted,
        long heapMax,
        long nonHeapUsed,
        long nonHeapCommitted,
        long nonHeapMax,
        long totalMaxMemory,
        long freeMemory
) {
}
