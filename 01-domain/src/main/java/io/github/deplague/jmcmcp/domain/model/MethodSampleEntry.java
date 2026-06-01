package io.github.deplague.jmcmcp.domain.model;

/**
 * Method sample entry for CPU analysis.
 */
public record MethodSampleEntry(
        String method,
        long samples
) {
}
