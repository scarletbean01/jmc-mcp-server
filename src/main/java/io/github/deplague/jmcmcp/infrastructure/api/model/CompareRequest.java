package io.github.deplague.jmcmcp.infrastructure.api.model;

/**
 * Request body for recording comparison endpoints.
 */
public record CompareRequest(
        String baselineRecordingId,
        String comparisonRecordingId,
        String analysisType,
        String startTime,
        String endTime
) {
}
