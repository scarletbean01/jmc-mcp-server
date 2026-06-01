package io.github.deplague.jmcmcp.domain.model;

import java.util.Map;

/**
 * A single stack-trace match found by the search.
 */
public record StackTraceMatchEntry(
        String eventType,
        String timestamp,
        String threadName,
        String fullTrace,
        Map<String, String> details
) {
}
