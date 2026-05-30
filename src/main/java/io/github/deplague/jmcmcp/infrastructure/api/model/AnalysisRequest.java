package io.github.deplague.jmcmcp.infrastructure.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

/**
 * Request body for analysis endpoints.
 * Supports optional time filtering and analysis-specific parameters.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AnalysisRequest(
        String startTime,
        String endTime,
        Map<String, Object> params
) {
    public AnalysisRequest {
        params = params != null ? params : Map.of();
    }
}
