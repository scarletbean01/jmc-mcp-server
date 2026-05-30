package io.github.deplague.jmcmcp.infrastructure.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

/**
 * Status of an asynchronous analysis job.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record JobStatusResponse(
        String jobId,
        String status,
        Object result,
        String error,
        Instant createdAt,
        Instant completedAt,
        int progressPercent
) {
}
