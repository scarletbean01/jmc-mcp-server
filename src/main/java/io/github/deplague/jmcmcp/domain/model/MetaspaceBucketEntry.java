package io.github.deplague.jmcmcp.domain.model;

/**
 * Metaspace usage per time bucket.
 */
public record MetaspaceBucketEntry(
        long bucketStartMs,
        Long minUsedBytes,
        Long avgUsedBytes,
        Long maxUsedBytes,
        Long minCommittedBytes,
        Long avgCommittedBytes,
        Long maxCommittedBytes
) {
}
