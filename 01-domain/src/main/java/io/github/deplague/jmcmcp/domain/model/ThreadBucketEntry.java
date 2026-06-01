package io.github.deplague.jmcmcp.domain.model;

/**
 * Thread count per time bucket.
 */
public record ThreadBucketEntry(
        long bucketStartMs,
        Long minCount,
        Long avgCount,
        Long maxCount
) {
}
