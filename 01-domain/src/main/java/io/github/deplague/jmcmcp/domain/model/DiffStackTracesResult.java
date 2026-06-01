package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of comparing hot methods between two JFR recordings.
 */
public record DiffStackTracesResult(
        double baselineDurationSec,
        double targetDurationSec,
        long baselineTotalSamples,
        long targetTotalSamples,
        List<MethodDiffEntry> newMethods,
        List<MethodDiffEntry> disappearedMethods,
        List<MethodDiffEntry> changedMethods,
        List<MethodDiffEntry> stableMethods
) {
}
