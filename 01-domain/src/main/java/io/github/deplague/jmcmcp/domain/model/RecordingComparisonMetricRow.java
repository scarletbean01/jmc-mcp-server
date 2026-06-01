package io.github.deplague.jmcmcp.domain.model;

/**
 * A single metric row in a recording comparison category.
 */
public record RecordingComparisonMetricRow(
        String label,
        Double baselineValue,
        String baselineDisplay,
        Double targetValue,
        String targetDisplay,
        Double deltaPercent,
        boolean normalized,
        String indicator
) {
}
