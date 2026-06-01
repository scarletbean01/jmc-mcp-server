package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Thread lifecycle summary including start/end counts and creation sites.
 */
public record ThreadLifecycle(
        long startedCount,
        long endedCount,
        long netChange,
        List<ThreadCreationSite> creationSites
) {
}
