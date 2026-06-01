package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of GC recommendations analysis containing pause distribution, causes, warnings and recommendations.
 */
public record GcRecommendationsResult(
        String gcAlgorithm,
        PauseDistribution pauseDistribution,
        List<GcCauseEntry> youngCauses,
        List<GcCauseEntry> oldCauses,
        Double fullGcRatio,
        Long explicitGcCount,
        HeapUtilization heapUtilization,
        MetaspaceUtilization metaspaceUtilization,
        List<String> warnings,
        List<String> recommendations
) {
}
