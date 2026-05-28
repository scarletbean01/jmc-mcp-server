package io.github.deplague.jmcmcp.domain.model;

import java.util.Map;

/**
 * Per-thread blocking statistics.
 */
public record ThreadBlockingEntry(
        String threadName,
        String totalBlockedTime,
        long eventCount,
        String topCategory,
        Map<String, Long> categoryNanos
) {
}
