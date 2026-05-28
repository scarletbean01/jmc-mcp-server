package io.github.deplague.jmcmcp.domain.model;

/**
 * Represents a delta between baseline and target for a single key.
 */
public record RecordingComparisonDelta(
    String key,
    double baselineRate,
    double targetRate,
    double delta
) {
}
