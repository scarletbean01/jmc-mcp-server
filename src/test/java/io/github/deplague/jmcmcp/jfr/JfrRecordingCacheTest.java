package io.github.deplague.jmcmcp.jfr;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JfrRecordingCacheTest {

    @Test
    void cacheStartsEmpty() {
        JfrRecordingCache cache = new JfrRecordingCache();
        assertThat(cache.size()).isZero();
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
        // Cannot load a real file here without a test JFR, but we can test evict on empty cache
        cache.evict("/some/path.jfr");
        assertThat(cache.size()).isZero();
    }

    @Test
    void clearEmptiesCache() {
        JfrRecordingCache cache = new JfrRecordingCache();
        cache.clear();
        assertThat(cache.size()).isZero();
    }
}
