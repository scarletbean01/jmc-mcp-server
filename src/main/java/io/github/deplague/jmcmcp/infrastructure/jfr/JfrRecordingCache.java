package io.github.deplague.jmcmcp.infrastructure.jfr;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Weigher;
import io.github.deplague.jmcmcp.infrastructure.api.metrics.AnalysisMetrics;
import io.github.deplague.jmcmcp.domain.exception.AnalysisFailedException;
import io.github.deplague.jmcmcp.domain.exception.RecordingNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.flightrecorder.JfrLoaderToolkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Enterprise-grade cache for loaded JFR recordings using Caffeine and
 * platform-thread offloading for CPU-bound parsing to avoid Virtual Thread pinning.
 */
@ApplicationScoped
public final class JfrRecordingCache {

    private static final Logger LOG = LoggerFactory.getLogger(JfrRecordingCache.class);

    private static final long DEFAULT_TTL_MINUTES =
            Long.getLong("jmc.cache.ttl.minutes", 60);
    private static final long REFRESH_AFTER_WRITE_MINUTES =
            Long.getLong("jmc.cache.refresh.minutes", 30);
    private static final double HEAP_MULTIPLIER_ESTIMATE =
            Double.parseDouble(System.getProperty("jmc.cache.heap.multiplier", "3.5"));
    private static final int MAX_WEIGHT_PERCENT =
            Integer.getInteger("jmc.cache.max-weight-percent", 50);

    private static final long MAX_CACHE_WEIGHT =
            Runtime.getRuntime().maxMemory() * Math.max(1, Math.min(100, MAX_WEIGHT_PERCENT)) / 100;

    // Dedicated pool for CPU-bound JFR parsing so we don't pin virtual thread carriers
    private final ExecutorService parsingExecutor = Executors.newWorkStealingPool(
            Math.max(4, Runtime.getRuntime().availableProcessors())
    );

    private final Cache<String, CacheEntry> cache;

    // Statistics
    private final AtomicLong hitCount = new AtomicLong(0);
    private final AtomicLong missCount = new AtomicLong(0);
    private final AtomicLong evictionCount = new AtomicLong(0);

    private final AnalysisMetrics metrics;

    public JfrRecordingCache() {
        this(null);
    }

    public JfrRecordingCache(AnalysisMetrics metrics) {
        this(DEFAULT_TTL_MINUTES, metrics);
    }

    public JfrRecordingCache(long ttlMinutes, AnalysisMetrics metrics) {
        this.metrics = metrics;
        this.cache = Caffeine.<String, CacheEntry>newBuilder()
                .expireAfterAccess(Duration.ofMinutes(ttlMinutes))
                .maximumWeight(MAX_CACHE_WEIGHT)
                .weigher((Weigher<String, CacheEntry>) (key, value) -> (int) Math.min(Integer.MAX_VALUE, value.estimatedHeapWeight))
                .removalListener((key, value, cause) -> {
                    evictionCount.incrementAndGet();
                    if (metrics != null) {
                        metrics.recordCacheEviction();
                    }
                    if (value != null && value.estimatedHeapWeight > 512 * 1024 * 1024L) {
                        System.gc();
                    }
                    LOG.info("Evicted recording: {} (cause: {}, weight={})", key, cause,
                            value != null ? formatBytes(value.estimatedHeapWeight) : "?");
                })
                .recordStats()
                .build();
        LOG.info("JfrRecordingCache initialized: TTL={}min, MaxWeight={}",
                ttlMinutes, formatBytes(MAX_CACHE_WEIGHT));
    }

    public IItemCollection load(String filePath) throws IOException {
        File file = new File(filePath).getAbsoluteFile();
        String key = file.getAbsolutePath();

        CacheEntry entry = cache.getIfPresent(key);
        if (entry != null) {
            if (file.lastModified() == entry.fileLastModified && file.length() == entry.fileSize) {
                hitCount.incrementAndGet();
                if (metrics != null) {
                    metrics.recordCacheHit();
                }
                LOG.debug("Cache hit for recording: {}", key);
                return entry.collection;
            }
            // File changed, invalidate
            cache.invalidate(key);
            LOG.info("Cache invalidated for recording (file changed): {}", key);
        }

        if (!file.exists() || !file.isFile()) {
            throw new RecordingNotFoundException("JFR file does not exist or is not a regular file: " + filePath);
        }

        missCount.incrementAndGet();
        if (metrics != null) {
            metrics.recordCacheMiss();
        }
        LOG.info("Loading JFR recording: {} (size={})", key, formatBytes(file.length()));

        IItemCollection events;
        try {
            // Offload CPU-heavy parsing to platform threads to avoid pinning virtual thread carriers
            events = CompletableFuture.supplyAsync(() -> {
                try {
                    return JfrLoaderToolkit.loadEvents(file);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, parsingExecutor).join();
        } catch (Exception e) {
            throw new AnalysisFailedException("Failed to load JFR recording: " + filePath, e);
        }

        long eventCount = countEvents(events);
        CacheEntry newEntry = new CacheEntry(events, file.lastModified(), file.length(), eventCount);
        cache.put(key, newEntry);
        
        LOG.info("Loaded recording into cache: {} ({} events, estWeight={})", key, eventCount, formatBytes(newEntry.estimatedHeapWeight));
        return events;
    }

    public void evict(String filePath) {
        File file = new File(filePath).getAbsoluteFile();
        cache.invalidate(file.getAbsolutePath());
    }

    public void clear() {
        cache.invalidateAll();
    }

    public int size() {
        cache.cleanUp();
        return (int) cache.estimatedSize();
    }

    public long getHitCount() {
        return hitCount.get();
    }

    public long getMissCount() {
        return missCount.get();
    }

    public long getEvictionCount() {
        return evictionCount.get();
    }

    public long getTotalCachedBytes() {
        return cache.asMap().values().stream().mapToLong(e -> e.fileSize).sum();
    }

    public void shutdown() {
        parsingExecutor.shutdown();
        try {
            if (!parsingExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                parsingExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            parsingExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private static long countEvents(IItemCollection events) {
        long count = 0;
        for (IItemIterable iterable : events) {
            count += iterable.getItemCount();
        }
        return count;
    }

    private static String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.2f %sB", bytes / Math.pow(1024, exp), pre);
    }

    private static final class CacheEntry {
        final IItemCollection collection;
        final long fileLastModified;
        final long fileSize;
        final long estimatedHeapWeight;

        CacheEntry(IItemCollection collection, long fileLastModified, long fileSize, long eventCount) {
            this.collection = collection;
            this.fileLastModified = fileLastModified;
            this.fileSize = fileSize;
            double densityFactor = 1.0 + Math.log1p(eventCount / Math.max(1.0, fileSize / 1024.0)) / 10.0;
            this.estimatedHeapWeight = (long) (fileSize * HEAP_MULTIPLIER_ESTIMATE * densityFactor);
        }
    }
}
