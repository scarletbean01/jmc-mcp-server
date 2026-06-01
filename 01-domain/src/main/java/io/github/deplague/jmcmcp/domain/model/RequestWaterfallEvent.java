package io.github.deplague.jmcmcp.domain.model;

/**
 * Represents a single event in a request waterfall timeline.
 */
public record RequestWaterfallEvent(
        long timeMs,
        String eventType,
        String phase,
        long durationMs,
        String detail,
        String topFrame,
        String fullTrace,
        String threadName
) {
}
