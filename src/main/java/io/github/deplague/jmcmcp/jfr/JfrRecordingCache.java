package io.github.deplague.jmcmcp.jfr;

import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.flightrecorder.JfrLoaderToolkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Enterprise-grade cache for loaded JFR recordings with TTL, file-change
 * detection, memory-pressure eviction, and usage statistics.
 *
 * <p>Features:</p>
 * <ul>
 *   <li><b>TTL eviction:</b> Entries expire after a configurable duration (default 1h)</li>
 *   <li><b>File-change detection:</b> If the JFR file is modified after caching, the
 *       entry is invalidated on next access</li>
 *   <li><b>Memory-pressure eviction:</b> Uses {@link SoftReference} so the GC can
 *       reclaim cached recordings under heap pressure</li>
 *   <li><b>Statistics:</b> Tracks hits, misses, evictions, and total cached bytes</li>
 * </ul>
 */
public final class JfrRecordingCache {

    private static final Logger LOG = LoggerFactory.getLogger(JfrRecordingCache.class);

    private static final long DEFAULT_TTL_MINUTES = 60;
    private static final long CLEANUP_INTERVAL_MINUTES = 5;
    private static final double MEMORY_PRESSURE_THRESHOLD = 0.85;

    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private final long ttlMinutes;
    private final ScheduledExecutorService cleanupExecutor;

    // Statistics
    private final AtomicLong hitCount = new AtomicLong(0);
    private final AtomicLong missCount = new AtomicLong(0);
    private final AtomicLong evictionCount = new AtomicLong(0);

    public JfrRecordingCache() {
        this(DEFAULT_TTL_MINUTES);
    }

    public JfrRecordingCache(long ttlMinutes) {
        this.ttlMinutes = ttlMinutes;
        this.cleanupExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "jmc-recording-cache-cleanup");
            t.setDaemon(true);
            return t;
        });
        this.cleanupExecutor.scheduleWithFixedDelay(
                this::cleanupExpiredEntries,
                CLEANUP_INTERVAL_MINUTES, CLEANUP_INTERVAL_MINUTES, TimeUnit.MINUTES
        );
        LOG.info("JfrRecordingCache initialized: TTL={}min", ttlMinutes);
    }

    /**
     * Load a JFR recording from the given file path, using the cache if available
     * and the file has not changed since caching.
     *
     * @param filePath path to the .jfr file
     * @return the loaded item collection
     * @throws IOException if the file cannot be read or parsed
     */
    public IItemCollection load(String filePath) throws IOException {
        File file = new File(filePath).getAbsoluteFile();
        String key = file.getAbsolutePath();

        // Check for memory pressure and proactively evict if needed
        if (isUnderMemoryPressure()) {
            evictLargestUnderPressure();
        }

        CacheEntry entry = cache.get(key);
        if (entry != null) {
            IItemCollection cached = entry.getValidCollection(file);
            if (cached != null) {
                hitCount.incrementAndGet();
                LOG.debug("Cache hit for recording: {} (age={}ms)", key, entry.ageMillis());
                return cached;
            }
            // File changed or expired — remove stale entry
            cache.remove(key);
            evictionCount.incrementAndGet();
            LOG.info("Cache invalidated for recording (changed or expired): {}", key);
        }

        if (!file.exists() || !file.isFile()) {
            throw new IOException("JFR file does not exist or is not a regular file: " + filePath);
        }

        LOG.info("Loading JFR recording: {} (size={})", key, formatBytes(file.length()));
        missCount.incrementAndGet();

        IItemCollection events;
        try {
            events = JfrLoaderToolkit.loadEvents(file);
        } catch (org.openjdk.jmc.flightrecorder.CouldNotLoadRecordingException e) {
            throw new IOException("Failed to load JFR recording: " + filePath, e);
        }

        cache.put(key, new CacheEntry(events, file.lastModified(), file.length()));
        LOG.info("Loaded recording into cache: {} ({} events)", key,
                events.iterator().hasNext() ? "non-empty" : "empty");
        return events;
    }

    /**
     * Evict a specific recording from the cache.
     */
    public void evict(String filePath) {
        File file = new File(filePath).getAbsoluteFile();
        CacheEntry removed = cache.remove(file.getAbsolutePath());
        if (removed != null) {
            evictionCount.incrementAndGet();
        }
    }

    /**
     * Clear the entire cache.
     */
    public void clear() {
        int count = cache.size();
        cache.clear();
        evictionCount.addAndGet(count);
    }

    /**
     * @return number of cached recordings
     */
    public int size() {
        return cache.size();
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

    /**
     * @return total bytes of cached JFR files on disk (not heap usage)
     */
    public long getTotalCachedBytes() {
        return cache.values().stream()
                .mapToLong(CacheEntry::fileSize)
                .sum();
    }

    /**
     * Shutdown the cleanup executor.
     */
    public void shutdown() {
        cleanupExecutor.shutdown();
        try {
            if (!cleanupExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                cleanupExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            cleanupExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private boolean isUnderMemoryPressure() {
        Runtime runtime = Runtime.getRuntime();
        long max = runtime.maxMemory();
        long used = runtime.totalMemory() - runtime.freeMemory();
        return max > 0 && (double) used / max > MEMORY_PRESSURE_THRESHOLD;
    }

    private void evictLargestUnderPressure() {
        String largestKey = null;
        long largestSize = 0;
        for (var e : cache.entrySet()) {
            if (e.getValue().fileSize() > largestSize) {
                largestSize = e.getValue().fileSize();
                largestKey = e.getKey();
            }
        }
        if (largestKey != null) {
            cache.remove(largestKey);
            evictionCount.incrementAndGet();
            LOG.warn("Memory pressure detected — evicted largest recording: {} ({})",
                    largestKey, formatBytes(largestSize));
        }
    }

    private void cleanupExpiredEntries() {
        Instant cutoff = Instant.now().minus(Duration.ofMinutes(ttlMinutes));
        int removed = 0;
        for (var entry : cache.entrySet()) {
            if (entry.getValue().createdAt().isBefore(cutoff)) {
                cache.remove(entry.getKey());
                removed++;
            }
        }
        if (removed > 0) {
            evictionCount.addAndGet(removed);
            LOG.debug("Cleaned up {} expired recording cache entries", removed);
        }
    }

    private static String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.2f %sB", bytes / Math.pow(1024, exp), pre);
    }

    /**
     * Internal cache entry that wraps the recording in a SoftReference and
     * tracks file metadata for change detection.
     */
    private static final class CacheEntry {
        private final SoftReference<IItemCollection> ref;
        private final long fileLastModified;
        private final long fileSize;
        private final Instant createdAt;

        CacheEntry(IItemCollection collection, long fileLastModified, long fileSize) {
            this.ref = new SoftReference<>(collection);
            this.fileLastModified = fileLastModified;
            this.fileSize = fileSize;
            this.createdAt = Instant.now();
        }

        /**
         * Returns the cached collection if it is still present (not GC'd) and
         * the underlying file has not changed since caching.
         */
        IItemCollection getValidCollection(File file) {
            IItemCollection col = ref.get();
            if (col == null) {
                return null; // GC reclaimed
            }
            if (file.lastModified() != fileLastModified || file.length() != fileSize) {
                return null; // File changed
            }
            return col;
        }

        long fileSize() {
            return fileSize;
        }

        Instant createdAt() {
            return createdAt;
        }

        long ageMillis() {
            return System.currentTimeMillis() - createdAt.toEpochMilli();
        }
    }
}
