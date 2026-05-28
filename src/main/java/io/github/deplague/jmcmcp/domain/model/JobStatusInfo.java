package io.github.deplague.jmcmcp.domain.model;

import io.github.deplague.jmcmcp.async.JobStatus;
import java.time.Instant;

/**
 * Immutable snapshot of job status data for display.
 */
public record JobStatusInfo(
        String jobId,
        String toolName,
        JobStatus status,
        int progressPercent,
        String progressMessage,
        Instant createdAt,
        Instant startedAt,
        Instant completedAt,
        long durationMillis,
        String errorMessage
) {
}
