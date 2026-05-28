package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of CPU flame graph analysis containing state distribution, call paths, and hot methods.
 */
public record CpuFlameResult(
    long totalSamples,
    List<StateDistributionEntry> stateDistribution,
    List<CallPathEntry> callPaths,
    List<CpuFlameMethodEntry> hotMethods
) {
}
