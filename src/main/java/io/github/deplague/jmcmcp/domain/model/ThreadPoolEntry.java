package io.github.deplague.jmcmcp.domain.model;

/**
 * Per-thread-pool breakdown.
 */
public record ThreadPoolEntry(
        String poolPrefix,
        int threadCount,
        long cpuSamples,
        long blockedTimeMs,
        long blockedCount,
        long monitorEnterCount,
        long monitorWaitCount,
        long parkCount,
        long sleepCount,
        double activeRatio,
        String status
) {
}
