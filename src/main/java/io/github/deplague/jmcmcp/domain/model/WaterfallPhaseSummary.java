package io.github.deplague.jmcmcp.domain.model;

/**
 * Summary of time and event count for a single phase in a waterfall.
 */
public record WaterfallPhaseSummary(
        String phaseName,
        long totalTimeMs,
        int eventCount
) {
}
