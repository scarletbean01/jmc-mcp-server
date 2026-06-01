package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of safepoint and STW pause analysis.
 */
public record SafepointAnalysisResult(
        boolean hasData,
        long safepointCount,
        long totalNanos,
        long avgNanos,
        long maxNanos,
        long p95Nanos,
        List<SafepointCauseEntry> causeDistribution,
        List<TopSafepointEntry> topSafepoints,
        VmOperationSummary vmOperationSummary,
        TtspSummary ttspSummary
) {
}
