package io.github.deplague.jmcmcp.domain.model;

/**
 * A single code cache segment snapshot.
 */
public record CodeCacheSegment(
        String name,
        long entryCount,
        long methodCount,
        String reservedCapacity,
        String unallocatedCapacity,
        double utilizationPercent
) {
}
