package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Thread park summary.
 */
public record ThreadParkSummary(
        long count,
        String avgDuration,
        String maxDuration,
        List<ParkSiteEntry> topSites
) {
}
