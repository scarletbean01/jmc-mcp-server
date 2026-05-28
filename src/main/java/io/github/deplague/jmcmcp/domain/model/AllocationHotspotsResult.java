package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of allocation hotspots analysis containing ranked entries.
 */
public record AllocationHotspotsResult(
    boolean hasData,
    List<AllocationHotspotEntry> entries
) {
}
