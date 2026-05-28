package io.github.deplague.jmcmcp.domain.model;

/**
 * Recording cache statistics snapshot.
 */
public record RecordingCacheInfo(
        int size,
        long hitCount,
        long missCount,
        long evictionCount,
        long totalCachedBytes
) {
}
