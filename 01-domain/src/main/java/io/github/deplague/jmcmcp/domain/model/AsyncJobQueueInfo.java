package io.github.deplague.jmcmcp.domain.model;

/**
 * Async job queue statistics snapshot.
 */
public record AsyncJobQueueInfo(
        long activeJobs,
        long pendingJobs,
        long completedJobs,
        long failedJobs,
        int totalJobs,
        int recommendedPollSeconds
) {
}
