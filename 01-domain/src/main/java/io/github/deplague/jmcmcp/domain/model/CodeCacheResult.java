package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of code cache and JIT analysis.
 */
public record CodeCacheResult(
        List<CodeCacheSegment> segments,
        CompilerStats compilerStats,
        boolean hasCodeCacheEvents,
        boolean hasCompilerEvents
) {

    public boolean hasAnyData() {
        return hasCodeCacheEvents || hasCompilerEvents;
    }
}
