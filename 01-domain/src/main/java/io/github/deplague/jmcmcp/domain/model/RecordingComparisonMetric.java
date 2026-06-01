package io.github.deplague.jmcmcp.domain.model;

/**
 * Definition of a metric to compare between two recordings.
 */
public record RecordingComparisonMetric(
    String category,
    String label,
    String eventId,
    String attrId,
    RecordingComparisonAggType type,
    boolean normalize
) {
}
