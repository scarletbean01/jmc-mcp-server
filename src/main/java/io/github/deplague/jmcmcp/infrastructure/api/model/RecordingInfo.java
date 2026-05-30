package io.github.deplague.jmcmcp.infrastructure.api.model;

import java.time.Instant;
import java.util.Map;

/**
 * Metadata about an uploaded JFR recording.
 */
public record RecordingInfo(
        String recordingId,
        String fileName,
        long fileSize,
        Instant uploadedAt,
        double durationSeconds,
        long totalEvents,
        Map<String, String> availability
) {
}
