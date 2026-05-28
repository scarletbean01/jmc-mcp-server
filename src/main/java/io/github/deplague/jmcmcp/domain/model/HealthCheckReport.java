package io.github.deplague.jmcmcp.domain.model;

import java.time.Instant;

/**
 * Aggregated result of a server health check.
 */
public record HealthCheckReport(
        String status,
        String uptime,
        Instant serverStart,
        String jvmName,
        String javaVersion,
        double heapUsedPct,
        JvmMemoryInfo jvmMemory,
        JvmThreadInfo jvmThreads,
        RecordingCacheInfo recordingCache,
        AsyncJobQueueInfo asyncJobQueue
) {
}
