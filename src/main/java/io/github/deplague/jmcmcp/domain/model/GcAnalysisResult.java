package io.github.deplague.jmcmcp.domain.model;

import java.util.Optional;

/**
 * Result of GC analysis.
 */
public record GcAnalysisResult(
        Optional<GcPauseTimes> pauseTimes,
        Optional<GcFrequencies> frequencies,
        Optional<GcHeapSummary> heapSummary,
        boolean hasData
) {
}
