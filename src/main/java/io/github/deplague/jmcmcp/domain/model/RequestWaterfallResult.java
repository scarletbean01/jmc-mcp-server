package io.github.deplague.jmcmcp.domain.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Result of request waterfall analysis containing events, phase summaries,
 * event type counts, and timeline metadata.
 */
public record RequestWaterfallResult(
        List<RequestWaterfallEvent> events,
        Map<String, Long> eventTypeCounts,
        List<WaterfallPhaseSummary> phaseSummaries,
        Set<String> matchedThreads,
        long baseTimeMs,
        long endTimeMs,
        boolean hasResults
) {
}
