package io.github.deplague.jmcmcp.domain.model;

/**
 * Lightweight recording metadata used in comparison results.
 */
public record RecordingComparisonRecordingInfo(
        String path,
        double durationSeconds
) {
}
