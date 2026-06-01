package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of the smart thread starvation detector analysis.
 */
public record ThreadStarvationResult(
        CpuLoadSummary cpuLoad,
        int activeThreadCount,
        long totalBlockedEvents,
        List<BlockedPoolEntry> topBlockedPools,
        ThreadDumpSummary threadDump,
        ConnectionPoolSummary connectionPool,
        String primaryDiagnosis,
        List<String> findings,
        String agentHint
) {
}
