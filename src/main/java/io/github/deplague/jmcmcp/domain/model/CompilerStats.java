package io.github.deplague.jmcmcp.domain.model;

import java.util.Optional;

/**
 * JIT compiler statistics.
 */
public record CompilerStats(
        Optional<String> totalCompilations,
        Optional<String> peakCompilationTime,
        Optional<String> totalCompilationTime,
        Optional<Double> averageCompilationTimeMs
) {
}
