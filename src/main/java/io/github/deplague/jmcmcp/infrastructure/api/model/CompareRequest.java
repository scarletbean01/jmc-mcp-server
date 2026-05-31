package io.github.deplague.jmcmcp.infrastructure.api.model;

import java.util.Map;

/**
 * Request body for recording comparison endpoints.
 */
public record CompareRequest(
        String baselineRecordingId,
        String comparisonRecordingId,
        String analysisType,
        String startTime,
        String endTime,
        Map<String, Object> params
) {
    public CompareRequest {
        params = params != null ? params : Map.of();
    }
}
