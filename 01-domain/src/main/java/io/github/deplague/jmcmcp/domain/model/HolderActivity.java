package io.github.deplague.jmcmcp.domain.model;

/**
 * Describes what a lock holder thread was doing while holding the lock.
 */
public record HolderActivity(
        String description,
        String topFrame,
        boolean hasIo,
        boolean hasSql
) {
}
