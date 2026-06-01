package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of deadlock detection analysis.
 */
public record DeadlockDetectionResult(
        List<DeadlockCycle> deadlocks,
        int threadsAnalyzed,
        int threadsWithLocks,
        int threadsWaiting,
        int totalMonitors,
        long monitorEnterCount,
        long monitorWaitCount,
        boolean hasThreadDumps,
        boolean hasData
) {

    public boolean hasDeadlocks() {
        return deadlocks != null && !deadlocks.isEmpty();
    }
}
