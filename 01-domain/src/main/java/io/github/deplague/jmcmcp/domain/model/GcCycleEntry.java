package io.github.deplague.jmcmcp.domain.model;

/**
 * Heap usage per GC cycle.
 */
public record GcCycleEntry(
        long gcId,
        String heapUsed,
        String heapSize
) {
}
