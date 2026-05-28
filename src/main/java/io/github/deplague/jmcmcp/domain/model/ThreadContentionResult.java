package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of thread contention analysis.
 */
public record ThreadContentionResult(
        List<ContentionEntry> topContentions,
        String topLock,
        String topDuration,
        boolean hasData
) {
}
