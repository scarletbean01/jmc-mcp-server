package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of I/O hotspots analysis.
 */
public record IoHotspotsResult(
        List<IoEndpointEntry> fileEndpoints,
        List<IoEndpointEntry> socketEndpoints,
        List<IoLatencyPercentile> percentiles,
        boolean hasFileData,
        boolean hasSocketData
) {
}
