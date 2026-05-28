package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of predictive leak analysis using linear regression on post-GC heap usage.
 */
public record PredictiveLeakResult(
        String verdict,
        double growthRateKBPerMin,
        double rSquared,
        double slope,
        double intercept,
        double currentHeapMB,
        Double maxHeapMB,
        Double heapUtilizationPct,
        int dataPointCount,
        Long recordingDurationMs,
        OomProjection oomProjection,
        List<LeakSuspectEntry> leakSuspects
) {
}
