package io.github.deplague.jmcmcp.domain.model;

/**
 * Thread state distribution entry.
 */
public record ThreadStateEntry(
        String state,
        long samples,
        double percent
) {
}
