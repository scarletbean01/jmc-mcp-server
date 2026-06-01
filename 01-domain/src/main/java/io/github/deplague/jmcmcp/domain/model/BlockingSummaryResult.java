package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of blocking summary analysis.
 */
public record BlockingSummaryResult(
        String totalBlockedTime,
        long totalBlockedEvents,
        List<ThreadBlockingEntry> perThreadBlocking,
        List<BlockingReasonEntry> topBlockingReasons,
        List<CategoryDistributionEntry> categoryDistribution,
        boolean monitorContentionDetected,
        boolean hasData
) {
}
