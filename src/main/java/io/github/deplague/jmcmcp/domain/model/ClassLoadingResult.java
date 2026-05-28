package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of class loading analysis.
 */
public record ClassLoadingResult(
        List<ClassLoadEntry> longestLoads,
        ClassLoadingStats stats,
        boolean hasClassLoadEvents,
        boolean hasStatsEvents
) {

    public boolean hasAnyData() {
        return hasClassLoadEvents || hasStatsEvents;
    }
}
