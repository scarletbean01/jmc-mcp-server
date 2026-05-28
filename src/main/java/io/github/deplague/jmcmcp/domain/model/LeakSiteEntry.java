package io.github.deplague.jmcmcp.domain.model;

/**
 * Leak allocation site entry from old object samples.
 */
public record LeakSiteEntry(
        String siteKey,
        long sampleCount
) {
}
