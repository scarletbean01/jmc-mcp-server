package io.github.deplague.jmcmcp.domain.model;

/**
 * Single event in an incident timeline.
 */
public record TimelineEventEntry(
        long timestampMillis,
        String formattedTime,
        String description,
        String typeId,
        boolean isAnchor
) {
}
