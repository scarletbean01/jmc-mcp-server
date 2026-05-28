package io.github.deplague.jmcmcp.domain.model;

import java.util.List;
import java.util.Optional;

/**
 * Result of JIT compilation analysis.
 */
public record JitCompilationResult(
        Optional<String> totalCompilations,
        Optional<String> avgCompilationDuration,
        Optional<String> maxCompilationDuration,
        List<CompilationEntry> longestCompilations,
        Optional<String> totalDeoptimizations,
        List<DeoptimizationEntry> topDeoptimizedMethods,
        List<CompilerFailureEntry> compilerFailures,
        boolean hasData
) {
}
