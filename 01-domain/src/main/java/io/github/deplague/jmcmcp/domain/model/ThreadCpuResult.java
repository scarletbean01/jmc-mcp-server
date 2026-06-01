package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of thread CPU analysis.
 */
public record ThreadCpuResult(
        long totalSamples,
        List<ThreadCpuEntry> threads,
        List<ThreadStateEntry> stateDistribution
) {
}
