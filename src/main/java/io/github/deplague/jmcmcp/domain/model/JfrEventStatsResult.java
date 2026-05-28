package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of JFR event statistics analysis.
 */
public record JfrEventStatsResult(
        String eventType,
        long totalEvents,
        List<EventFieldStats> numericFields,
        List<EventCategoricalField> categoricalFields,
        boolean hasData
) {
}
