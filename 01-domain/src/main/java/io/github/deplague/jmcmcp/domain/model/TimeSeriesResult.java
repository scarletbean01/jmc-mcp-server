package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of time-series performance trend analysis.
 *
 * @param bucketSize formatted bucket size string (e.g. "1m", "10s")
 * @param warning    optional warning message about bucket-size adjustment
 * @param buckets    list of aggregated time buckets
 * @param showCpu    whether CPU load was included in the analysis
 * @param showGc     whether GC pause data was included in the analysis
 * @param showAlloc  whether allocation data was included in the analysis
 */
public record TimeSeriesResult(
        String bucketSize,
        String warning,
        List<TimeSeriesBucketEntry> buckets,
        boolean showCpu,
        boolean showGc,
        boolean showAlloc
) {
}
