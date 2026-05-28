package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of allocation flame graph analysis containing total bytes and ranked entries.
 */
public record AllocationFlameResult(
    long totalBytes,
    String formattedTotalBytes,
    List<AllocationFlameEntry> entries
) {
}
