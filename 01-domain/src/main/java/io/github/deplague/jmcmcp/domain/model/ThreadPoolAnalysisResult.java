package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of thread pool analysis.
 */
public record ThreadPoolAnalysisResult(
        boolean hasData,
        List<ThreadPoolEntry> pools,
        List<String> warnings,
        List<String> recommendations
) {
}
