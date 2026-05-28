package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of lock flame graph analysis containing total duration and ranked entries.
 */
public record LockFlameResult(
    long totalNanos,
    String formattedTotalDuration,
    List<LockFlameEntry> entries
) {
}
