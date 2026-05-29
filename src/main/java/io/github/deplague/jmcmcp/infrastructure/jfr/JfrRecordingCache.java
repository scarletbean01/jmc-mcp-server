package io.github.deplague.jmcmcp.infrastructure.jfr;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Weigher;
import io.github.deplague.jmcmcp.domain.exception.AnalysisFailedException;
import io.github.deplague.jmcmcp.domain.exception.RecordingNotFoundException;
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
public final class JfrRecordingCache {

    private static final Logger LOG = LoggerFactory.getLogger(JfrRecordingCache.class);

    private static final long DEFAULT_TTL_MINUTES = 60;
    private static final double HEAP_MULTIPLIER_ESTIMATE = 4.0;
    
    // Use half of max memory as the maximum cache weight
    private static final long MAX_CACHE_WEIGHT = Runtime.getRuntime().maxMemory() / 2;

    // Dedicated pool for CPU-bound JFR parsing so we don't pin virtual thread carriers
    private final ExecutorService parsingExecutor = Executors.newFixedThreadPool(
            Math.max(2, Runtime.getRuntime().availableProcessors() / 2),
            r -> {
                Thread t = new Thread(r, "jfr-parser");
                t.setDaemon(true);
                return t;
            }
    );

    private final Cache<String, CacheEntry> cache;

    // Statistics
    private final AtomicLong hitCount = new AtomicLong(0);
    private final AtomicLong missCount = new AtomicLong(0);
    private final AtomicLong evictionCount = new AtomicLong(0);

    public JfrRecordingCache() {
        this(DEFAULT_TTL_MINUTES);
    }

    public JfrRecordingCache(long ttlMinutes) {
        this.cache = Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofMinutes(ttlMinutes))
                .maximumWeight(MAX_CACHE_WEIGHT)
                .weigher((Weigher<String, CacheEntry>) (key, value) -> (int) Math.min(Integer.MAX_VALUE, value.estimatedHeapWeight))
                .removalListener((key, value, cause) -> {
                    evictionCount.incrementAndGet();
                    LOG.info("Evicted recording: {} (cause: {})", key, cause);
                })
                .build();
        LOG.info("JfrRecordingCache initialized: TTL={}min, MaxWeight={}", ttlMinutes, formatBytes(MAX_CACHE_WEIGHT));
    }

    public IItemCollection load(String filePath) throws IOException {
        File file = new File(filePath).getAbsoluteFile();
        String key = file.getAbsolutePath();

        CacheEntry entry = cache.getIfPresent(key);
        if (entry != null) {
            if (file.lastModified() == entry.fileLastModified && file.length() == entry.fileSize) {
                hitCount.incrementAndGet();
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
