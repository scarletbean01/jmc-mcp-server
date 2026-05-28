package io.github.deplague.jmcmcp.domain.model;

import java.util.Optional;

/**
 * I/O summary statistics.
 */
public record IoSummary(
        Optional<String> eventCount,
        Optional<String> totalDuration,
        Optional<String> avgDuration,
        Optional<String> totalRead,
        Optional<String> totalWrite
) {
}
