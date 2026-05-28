package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of incident timeline analysis.
 */
public record IncidentTimelineResult(
        String anchorTime,
        int windowMs,
        boolean truncated,
        List<TimelineEventEntry> events
) {
}
