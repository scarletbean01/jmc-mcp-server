package io.github.deplague.jmcmcp.domain.model;

import java.util.List;
import java.util.Map;

/**
 * Structured result of a comprehensive A/B JFR recording comparison.
 * Suitable for JSON serialization by the REST adapter.
 */
public record RecordingComparisonResult(
        RecordingComparisonRecordingInfo baseline,
        RecordingComparisonRecordingInfo target,
        List<String> warnings,
        List<String> summary,
        Map<String, List<RecordingComparisonMetricRow>> metrics,
        RecordingComparisonRules ruleChanges,
        List<RecordingComparisonDelta> cpuDeltas,
        List<RecordingComparisonDelta> allocationDeltas,
        List<RecordingComparisonDelta> contentionDeltas,
        List<RecordingComparisonDelta> exceptionDeltas
) {
}
