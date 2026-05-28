package io.github.deplague.jmcmcp.domain.model;

import java.util.Optional;

/**
 * Class loading statistics snapshot.
 */
public record ClassLoadingStats(
        Optional<String> maxLoadedCount,
        Optional<String> maxUnloadedCount
) {
}
