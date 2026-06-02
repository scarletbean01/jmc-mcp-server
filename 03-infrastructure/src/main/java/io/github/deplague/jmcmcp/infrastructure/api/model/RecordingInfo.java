package io.github.deplague.jmcmcp.infrastructure.api.model;

import java.time.Instant;
import java.util.Map;

/**
 * Metadata about an uploaded JFR recording.
 */
public record RecordingInfo(
        String id,
        String filename,
        long size,
        Instant uploadTime,
        double durationSeconds,
        long totalEvents,
        Instant startTime,
        Instant endTime,
        Map<String, String> availability
) {
}
