package io.github.deplague.jmcmcp.domain.model;

/**
 * Revoked lock class entry.
 */
public record RevokedLockClassEntry(
        String lockClass,
        long count
) {
}
