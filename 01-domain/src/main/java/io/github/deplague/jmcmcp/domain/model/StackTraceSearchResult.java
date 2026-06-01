package io.github.deplague.jmcmcp.domain.model;

import java.util.List;
import java.util.Map;

/**
 * Result of a stack-trace search across JFR events.
 */
public record StackTraceSearchResult(
        String classPattern,
        String eventType,
        List<StackTraceMatchEntry> matches,
        Map<String, Long> distribution,
        int limit,
        boolean limited
) {
}
