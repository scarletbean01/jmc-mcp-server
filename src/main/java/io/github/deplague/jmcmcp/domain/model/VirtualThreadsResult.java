package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of virtual thread analysis.
 */
public record VirtualThreadsResult(
        long pinnedCount,
        long submitFailedCount,
        long sleepFailedCount,
        List<VirtualThreadPinningSite> pinningSites,
        List<VirtualThreadFailure> submitFailures,
        List<VirtualThreadFailure> sleepFailures,
        boolean hasData
) {
}
