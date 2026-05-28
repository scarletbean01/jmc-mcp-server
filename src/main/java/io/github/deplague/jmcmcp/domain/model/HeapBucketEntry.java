package io.github.deplague.jmcmcp.domain.model;

/**
 * Heap usage per time bucket.
 */
public record HeapBucketEntry(
        long bucketStartMs,
        Long minBytes,
        Long avgBytes,
        Long maxBytes
) {
}
