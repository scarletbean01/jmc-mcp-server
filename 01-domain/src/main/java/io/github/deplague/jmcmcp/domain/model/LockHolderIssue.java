package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * A single lock holder issue identified by the smart lock resolver.
 */
public record LockHolderIssue(
        String monitorClass,
        String holderName,
        int blockedThreadCount,
        long blockedCount,
        String totalBlockedDuration,
        HolderActivity holderActivity,
        List<BlockedTraceEntry> topBlockedTraces
) {
}
