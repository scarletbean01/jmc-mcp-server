package io.github.deplague.jmcmcp.domain.model;

import java.util.Map;

/**
 * Result of JFR recording overview analysis.
 */
public record JfrOverviewResult(
        String filePath,
        double durationSeconds,
        long totalEvents,
        Long filteredEvents,
        Map<String, Long> eventCounts,
        Map<String, String> availability
) {
}
