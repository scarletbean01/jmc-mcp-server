package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of exception analysis.
 */
public record ExceptionAnalysisResult(
        long totalExceptions,
        long totalErrors,
        List<ExceptionEntry> topExceptions,
        boolean hasData
) {
}
