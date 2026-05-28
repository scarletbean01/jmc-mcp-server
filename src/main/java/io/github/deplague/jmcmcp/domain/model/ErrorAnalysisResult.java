package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of error analysis.
 */
public record ErrorAnalysisResult(
        long totalErrors,
        long totalExceptions,
        List<ErrorEntry> topErrors,
        boolean hasData
) {
}
