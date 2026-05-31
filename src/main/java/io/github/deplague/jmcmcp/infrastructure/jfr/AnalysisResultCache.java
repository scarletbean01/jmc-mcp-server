package io.github.deplague.jmcmcp.infrastructure.jfr;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Cache for expensive analysis results keyed by (recordingId, analysisType, paramsHash).
 *
 * <p>Caches domain-level analysis outputs (e.g., {@code HotMethodsResult},
 * {@code GcAnalysisResult}) to avoid redundant recomputation on repeated queries.
 * Uses Caffeine with TTL-based expiration and size-based eviction.</p>
 *
 * <p>This lives in the infrastructure layer because it uses Caffeine (a framework).
 * The application layer may delegate to it for result caching.</p>
 */
@ApplicationScoped
public final class AnalysisResultCache {

    private static final Logger LOG = LoggerFactory.getLogger(AnalysisResultCache.class);

    private static final long DEFAULT_TTL_MINUTES =
            Long.getLong("jmc.analysis.cache.ttl.minutes", 15);
    private static final int MAX_ENTRIES =
            Integer.getInteger("jmc.analysis.cache.max-entries", 200);

    private final Cache<String, Object> resultCache;

    public AnalysisResultCache() {
        this(DEFAULT_TTL_MINUTES);
    }

    public AnalysisResultCache(long ttlMinutes) {
        this.resultCache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(ttlMinutes))
                .maximumSize(MAX_ENTRIES)
                .removalListener((key, value, cause) ->
                        LOG.debug("Evicted analysis result: {} (cause: {})", key, cause))
                .recordStats()
                .build();
        LOG.info("AnalysisResultCache initialized: TTL={}min, MaxEntries={}", ttlMinutes, MAX_ENTRIES);
    }

    /**
     * Build a deterministic cache key from the analysis parameters.
     *
     * @param recordingId  the recording identifier
     * @param analysisType the analysis type (e.g., "hot-methods", "gc-analysis")
     * @param paramsHash   a hash of analysis-specific parameters (startTime, endTime, filters, etc.)
     * @return a cache key string
     */
    public static String buildKey(String recordingId, String analysisType, String paramsHash) {
        return recordingId + "|" + analysisType + "|" + Objects.requireNonNullElse(paramsHash, "default");
    }

    /**
     * Retrieve a cached result or compute it if absent.
     *
     * @param key      the cache key
     * @param supplier the expensive computation supplier
     * @param <T>      the result type
     * @return the cached or freshly computed result
     */
    @SuppressWarnings("unchecked")
    public <T> T getOrCompute(String key, Supplier<T> supplier) {
        T result = (T) resultCache.getIfPresent(key);
        if (result != null) {
            LOG.debug("Analysis cache hit: {}", key);
            return result;
        }
        LOG.debug("Analysis cache miss: {}", key);
        result = supplier.get();
        if (result != null) {
            resultCache.put(key, result);
        }
        return result;
    }

    /**
     * Explicitly put a result into the cache.
     *
     * @param key    the cache key
     * @param result the result to cache
     */
    public void put(String key, Object result) {
        resultCache.put(key, result);
    }

    /**
     * Invalidate a specific cached result.
     *
     * @param key the cache key
     */
    public void invalidate(String key) {
        resultCache.invalidate(key);
    }

    /**
     * Invalidate all results for a given recording.
     *
     * @param recordingId the recording identifier
     */
    public void invalidateByRecording(String recordingId) {
        resultCache.asMap().keySet().removeIf(key -> key.startsWith(recordingId + "|"));
    }

    /**
     * Clear the entire cache.
     */
    public void clear() {
        resultCache.invalidateAll();
    }

    /**
     * @return number of cached results
     */
    public int size() {
        resultCache.cleanUp();
        return (int) resultCache.estimatedSize();
    }

    /**
     * @return Caffeine cache statistics (hit rate, eviction count, etc.)
     */
    public com.github.benmanes.caffeine.cache.stats.CacheStats stats() {
        return resultCache.stats();
    }
}
