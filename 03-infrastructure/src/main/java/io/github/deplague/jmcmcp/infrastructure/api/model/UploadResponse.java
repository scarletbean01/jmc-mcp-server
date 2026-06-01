package io.github.deplague.jmcmcp.infrastructure.api.model;

import java.time.Instant;

/**
 * Response returned after a JFR recording upload.
 */
public record UploadResponse(
        String id,
        String filename,
        long size,
        Instant uploadTime
) {
}
