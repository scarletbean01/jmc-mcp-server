package io.github.deplague.jmcmcp.domain.model;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

/**
 * Represents an asynchronous analysis job.
 */
public record AsyncJob(
        String jobId,
        String recordingId,
        String analysisType,
        String status,
        Object result,
        String error,
        Instant createdAt,
        Instant completedAt,
        int progressPercent,
        CompletableFuture<?> future
) {
}
