package io.github.deplague.jmcmcp.domain.model;

/**
 * Leaking class entry from old object samples.
 */
public record LeakingClassEntry(
        String className,
        long sampleCount
) {
}
