package io.github.deplague.jmcmcp.domain.model;

/**
 * A thread creation site with occurrence count.
 */
public record ThreadCreationSite(
        long count,
        String trace
) {
}
