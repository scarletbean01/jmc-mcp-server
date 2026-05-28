package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Result of heap, metaspace and thread trend analysis.
 */
public record HeapTrendsResult(
        String bucketSize,
        List<HeapBucketEntry> heapBuckets,
        List<MetaspaceBucketEntry> metaspaceBuckets,
        List<ThreadBucketEntry> threadBuckets,
        MetricSummary heapSummary,
        MetricSummary metaspaceSummary,
        MetricSummary threadSummary
) {
}
