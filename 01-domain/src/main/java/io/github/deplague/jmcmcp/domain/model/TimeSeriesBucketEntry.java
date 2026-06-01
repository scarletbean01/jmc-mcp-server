package io.github.deplague.jmcmcp.domain.model;

/**
 * A single time-series bucket containing aggregated metrics.
 *
 * @param bucketStartMs bucket start time in epoch milliseconds
 * @param cpuAvg        average CPU load as a fraction (0.0–1.0), or null if not requested
 * @param gcPauseSumNs  sum of GC pause durations in nanoseconds, or null if not requested
 * @param allocSumBytes sum of allocated bytes, or null if not requested
 */
public record TimeSeriesBucketEntry(
        long bucketStartMs,
        Double cpuAvg,
        Long gcPauseSumNs,
        Long allocSumBytes
) {
}
