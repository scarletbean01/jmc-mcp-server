package io.github.deplague.jmcmcp.domain.model;

/**
 * Leak suspect class from old object samples.
 */
public record LeakSuspectEntry(
        String className,
        long sampleCount,
        double percentage
) {
}
