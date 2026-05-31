package io.github.deplague.jmcmcp.infrastructure.api.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Request body for analysis endpoints.
 * Supports optional time filtering and analysis-specific parameters.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnalysisRequest {
    @JsonProperty("start-time")
    private String startTime;

    @JsonProperty("end-time")
    private String endTime;

    private Map<String, Object> params = new HashMap<>();

    @JsonAnySetter
    public void addParam(String key, Object value) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(key, value);
    }
}
