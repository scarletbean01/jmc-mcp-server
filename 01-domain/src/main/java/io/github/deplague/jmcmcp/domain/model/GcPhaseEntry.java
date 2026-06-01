package io.github.deplague.jmcmcp.domain.model;

/**
 * GC pause phase breakdown entry.
 */
public record GcPhaseEntry(
        String name,
        int count,
        String avg,
        String p95,
        String p99,
        String max
) {
}
