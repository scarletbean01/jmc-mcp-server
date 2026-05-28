package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of memory leak analysis from old object samples.
 */
public record MemoryLeaksResult(
        boolean hasData,
        long totalSampledObjects,
        List<LeakingClassEntry> leakingClasses,
        List<LeakSiteEntry> leakSites
) {
}
