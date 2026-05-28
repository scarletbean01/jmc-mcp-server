package io.github.deplague.jmcmcp.domain.model;

/**
 * Summary of young and old generation garbage collections.
 */
public record GenerationalSummary(
        long youngCount,
        String youngTotalDuration,
        String youngAvgDuration,
        long oldCount,
        String oldTotalDuration,
        String oldAvgDuration
) {
}
