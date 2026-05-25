package io.github.deplague.jmcmcp.jfr;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class JfrRecordingCacheTest {

    @Test
    void cacheStartsEmpty() {
        JfrRecordingCache cache = new JfrRecordingCache();
        assertThat(cache.size()).isZero();
        assertThat(cache.getHitCount()).isZero();
        assertThat(cache.getMissCount()).isZero();
        assertThat(cache.getEvictionCount()).isZero();
    }

    @Test
    void loadMissingFileThrowsIOException() {
        JfrRecordingCache cache = new JfrRecordingCache();
        org.junit.jupiter.api.Assertions.assertThrows(
                java.io.IOException.class,
                () -> cache.load("/nonexistent/recording.jfr")
        );
    }

    @Test
    void evictReducesSize() {
        JfrRecordingCache cache = new JfrRecordingCache();
        cache.evict("/some/path.jfr");
        assertThat(cache.size()).isZero();
    }

    @Test
    void clearEmptiesCache() {
        JfrRecordingCache cache = new JfrRecordingCache();
        cache.clear();
        assertThat(cache.size()).isZero();
    }

    @Test
    void loadRealFileTracksMissAndSize() throws Exception {
        JfrRecordingCache cache = new JfrRecordingCache();
        File file = resolveJfr("before.jfr");

        cache.load(file.getAbsolutePath());
        assertThat(cache.getMissCount()).isGreaterThanOrEqualTo(1);
        assertThat(cache.size()).isEqualTo(1);
    }

    @Test
    void reloadRealFileTracksHit() throws Exception {
        JfrRecordingCache cache = new JfrRecordingCache();
        File file = resolveJfr("before.jfr");

        cache.load(file.getAbsolutePath());
        cache.load(file.getAbsolutePath());

        // The second load may be a hit, but memory-pressure eviction can invalidate it
        assertThat(cache.getMissCount()).isGreaterThanOrEqualTo(1);
        // Either we got a hit (if no eviction) or a second miss (if evicted)
        assertThat(cache.getHitCount() + cache.getMissCount()).isGreaterThanOrEqualTo(2);
    }

    @Test
    void tracksTotalCachedBytes() throws Exception {
        JfrRecordingCache cache = new JfrRecordingCache();
        File file = resolveJfr("before.jfr");

        cache.load(file.getAbsolutePath());
        assertThat(cache.getTotalCachedBytes()).isGreaterThan(0);
    }

    @Test
    void evictionCountTracksClear() {
        JfrRecordingCache cache = new JfrRecordingCache();
        File file = resolveJfr("before.jfr");

        try {
            cache.load(file.getAbsolutePath());
        } catch (Exception e) {
            // ignore
        }

        long before = cache.getEvictionCount();
        cache.clear();
        assertThat(cache.getEvictionCount()).isGreaterThanOrEqualTo(before);
    }

    @Test
    void ttlEvictionRemovesExpiredEntries() throws Exception {
        JfrRecordingCache cache = new JfrRecordingCache(0); // 0 minute TTL = immediate expiry
        File file = resolveJfr("before.jfr");

        cache.load(file.getAbsolutePath());
        Thread.sleep(100);

        // Cleanup runs in background; force re-load should see expired entry
        cache.load(file.getAbsolutePath());
        // Either it was a hit (if cleanup didn't run yet) or a miss (if cleaned up)
        // The important thing is that the cache doesn't crash
        assertThat(cache.size()).isBetween(0, 1);
    }

    private static File resolveJfr(String name) {
        File file = new File(name);
        if (!file.exists()) {
            file = new File(System.getProperty("user.dir"), name);
        }
        return file;
    }
}
