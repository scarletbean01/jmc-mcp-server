package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of searching JFR events.
 */
public record SearchEventsResult(
        String eventType,
        String displayName,
        List<SearchEventEntry> events,
        boolean hasData
) {
}
