package io.github.deplague.jmcmcp.domain.model;

/**
 * GC frequency counts.
 */
public record GcFrequencies(
        long youngGCs,
        long oldGCs
) {
}
