package io.github.deplague.jmcmcp.domain.model;

import java.util.List;
import java.util.Optional;

/**
 * Result of direct buffer statistics analysis.
 */
public record DirectBufferResult(
        Optional<String> minCount,
        Optional<String> avgCount,
        Optional<String> maxCount,
        Optional<String> maxCapacity,
        Optional<String> maxUsed,
        Optional<String> maxDirectMemorySize,
        Optional<Double> maxUtilizationPercent,
        List<BufferSample> trend,
        boolean hasData
) {

    public boolean isHighUtilization() {
        return maxUtilizationPercent.isPresent() && maxUtilizationPercent.get() > 90.0;
    }

    /**
     * A single direct buffer sample.
     */
    public record BufferSample(long timestampMs, long count, long capacity, long used) {
    }
}
