package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Comprehensive result of detailed GC analysis.
 */
public record GcDetailResult(
        GcConfiguration config,
        GenerationalSummary generationalSummary,
        List<ReferenceStatEntry> referenceStats,
        Double referenceOverheadPct,
        List<GcCauseEntry> causeDistribution,
        List<GcPhaseEntry> phaseBreakdown,
        HeapTrendSummary heapTrendSummary,
        List<GcCycleEntry> gcCycles
) {
}
