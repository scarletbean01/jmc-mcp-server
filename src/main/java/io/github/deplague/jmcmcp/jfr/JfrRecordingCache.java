package io.github.deplague.jmcmcp.jfr;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.flightrecorder.JfrLoaderToolkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 *   <li><b>Weighted eviction:</b> Evicts enough entries to drop below a safe threshold,
 *       not just one. Weights factor in event density, not just file size</li>
 *   <li><b>Access tracking:</b> Tracks last-accessed time for LRU-style decisions</li>
 *   <li><b>Admission control:</b> Pre-load eviction when a new recording would push
 *       aggregate weight over the safe limit</li>
 *   <li><b>Statistics:</b> Tracks hits, misses, evictions, and total cached bytes</li>
 * </ul>
 */
public final class JfrRecordingCache {

    private static final Logger LOG = LoggerFactory.getLogger(
        JfrRecordingCache.class
    );

    private static final long DEFAULT_TTL_MINUTES = 60;
    private static final long CLEANUP_INTERVAL_MINUTES = 5;
    private static final double MEMORY_PRESSURE_THRESHOLD = 0.85;
    private static final double MEMORY_SAFE_THRESHOLD = 0.70;
    // Estimated heap multiplier: JFR parsing typically expands file size by 3–8x
    private static final double HEAP_MULTIPLIER_ESTIMATE = 4.0;

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
            CLEANUP_INTERVAL_MINUTES,
            CLEANUP_INTERVAL_MINUTES,
            TimeUnit.MINUTES
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

        CacheEntry entry = cache.get(key);
        if (entry != null) {
            IItemCollection cached = entry.getValidCollection(file);
            if (cached != null) {
                entry.touch();
                hitCount.incrementAndGet();
                LOG.debug(
                    "Cache hit for recording: {} (age={}ms, weight={})",
                    key,
                    entry.ageMillis(),
                    formatBytes(entry.estimatedHeapWeight())
                );
                return cached;
            }
            // File changed or expired — remove stale entry
            cache.remove(key);
            evictionCount.incrementAndGet();
            LOG.info(
                "Cache invalidated for recording (changed or expired): {}",
                key
            );
        }

        if (!file.exists() || !file.isFile()) {
            throw new IOException(
                "JFR file does not exist or is not a regular file: " + filePath
            );
        }

        // Pre-flight admission: evict proactively if this load would push us over safe threshold
        long fileSize = file.length();
        long estimatedWeight = (long) (fileSize * HEAP_MULTIPLIER_ESTIMATE);
        admitWithEviction(estimatedWeight);

        LOG.info(
            "Loading JFR recording: {} (size={}, estWeight={})",
            key,
            formatBytes(fileSize),
            formatBytes(estimatedWeight)
        );
        missCount.incrementAndGet();

        IItemCollection events;
        try {
            events = JfrLoaderToolkit.loadEvents(file);
        } catch (
            org.openjdk.jmc.flightrecorder.CouldNotLoadRecordingException e
        ) {
            throw new IOException(
                "Failed to load JFR recording: " + filePath,
                e
            );
        }

        long eventCount = countEvents(events);
        cache.put(
            key,
            new CacheEntry(events, file.lastModified(), fileSize, eventCount)
        );
        LOG.info(
            "Loaded recording into cache: {} ({} events, estWeight={})",
            key,
            eventCount,
            formatBytes(estimatedWeight)
        );
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
        return cache.values().stream().mapToLong(CacheEntry::fileSize).sum();
    }

    /**
     * @return total estimated heap weight of all cached recordings
     */
    public long getTotalEstimatedWeight() {
        return cache
            .values()
            .stream()
            .mapToLong(CacheEntry::estimatedHeapWeight)
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

    // ------------------------------------------------------------------
    // Admission & eviction control
    // ------------------------------------------------------------------

    /**
     * Before admitting a new recording of the given estimated weight, evict enough
     * existing entries so that total estimated weight stays below the safe threshold.
     * This prevents OOM races when multiple large recordings are loaded concurrently.
     */
    private void admitWithEviction(long incomingWeight) {
        Runtime runtime = Runtime.getRuntime();
        long max = runtime.maxMemory();
        if (max <= 0) return;

        long currentWeight = getTotalEstimatedWeight();
        long projectedUsed =
            runtime.totalMemory() -
            runtime.freeMemory() +
            currentWeight +
            incomingWeight;
        double projectedRatio = (double) projectedUsed / max;

        if (projectedRatio > MEMORY_SAFE_THRESHOLD || isUnderMemoryPressure()) {
            evictUntilUnderThreshold(MEMORY_SAFE_THRESHOLD, incomingWeight);
        }
    }

    /**
     * Evict entries (largest weight first, then oldest last-accessed) until the
     * total estimated weight plus the optional reserved headroom is below the target.
     */
    private void evictUntilUnderThreshold(
        double targetRatio,
        long reservedHeadroom
    ) {
        Runtime runtime = Runtime.getRuntime();
        long max = runtime.maxMemory();
        if (max <= 0) return;

        int evicted = 0;
        while (true) {
            long currentWeight = getTotalEstimatedWeight();
            long projectedUsed =
                runtime.totalMemory() -
                runtime.freeMemory() +
                currentWeight +
                reservedHeadroom;
            if ((double) projectedUsed / max <= targetRatio) {
                break;
            }
            if (cache.isEmpty()) {
                break;
            }

            // Evict by weight (desc), then by last access (oldest first), then by created time
            String victim = cache
                .entrySet()
                .stream()
                .min(
                    Comparator.<Map.Entry<String, CacheEntry>>comparingLong(e ->
                        e.getValue().estimatedHeapWeight()
                    )
                        .reversed()
                        .thenComparing(e -> e.getValue().lastAccessed)
                        .thenComparing(e -> e.getValue().createdAt)
                )
                .map(Map.Entry::getKey)
                .orElse(null);

            if (victim == null) break;

            CacheEntry removed = cache.remove(victim);
            if (removed != null) {
                evicted++;
                evictionCount.incrementAndGet();
                LOG.warn(
                    "Evicted recording from cache: {} (weight={}, lastAccessed={}ms ago)",
                    victim,
                    formatBytes(removed.estimatedHeapWeight()),
                    System.currentTimeMillis() - removed.lastAccessed
                );
            }
        }
        if (evicted > 0) {
            LOG.warn(
                "Proactive eviction complete: {} recordings removed to maintain safe heap threshold",
                evicted
            );
        }
    }

    private boolean isUnderMemoryPressure() {
        Runtime runtime = Runtime.getRuntime();
        long max = runtime.maxMemory();
        long used = runtime.totalMemory() - runtime.freeMemory();
        return max > 0 && (double) used / max > MEMORY_PRESSURE_THRESHOLD;
    }

    private void cleanupExpiredEntries() {
        Instant cutoff = Instant.now().minus(Duration.ofMinutes(ttlMinutes));
        int removed = 0;
        for (var entry : cache.entrySet()) {
            if (entry.getValue().createdAt.isBefore(cutoff)) {
                cache.remove(entry.getKey());
                removed++;
            }
        }
        if (removed > 0) {
            evictionCount.addAndGet(removed);
            LOG.debug("Cleaned up {} expired recording cache entries", removed);
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

    /**
     * Internal cache entry that wraps the recording in a SoftReference and
     * tracks file metadata for change detection, access frequency, and heap weight.
     */
    private static final class CacheEntry {

        private final SoftReference<IItemCollection> ref;
        private final long fileLastModified;
        private final long fileSize;
        private final long eventCount;
        private final long estimatedHeapWeight;
        private final Instant createdAt;
        private volatile long lastAccessed;

        CacheEntry(
            IItemCollection collection,
            long fileLastModified,
            long fileSize,
            long eventCount
        ) {
            this.ref = new SoftReference<>(collection);
            this.fileLastModified = fileLastModified;
            this.fileSize = fileSize;
            this.eventCount = eventCount;
            // Weight = file size expanded by heap multiplier, further scaled by event density
            double densityFactor =
                1.0 +
                Math.log1p(eventCount / Math.max(1.0, fileSize / 1024.0)) /
                    10.0;
            this.estimatedHeapWeight = (long) (fileSize *
                HEAP_MULTIPLIER_ESTIMATE *
                densityFactor);
            this.createdAt = Instant.now();
            this.lastAccessed = System.currentTimeMillis();
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
            if (
                file.lastModified() != fileLastModified ||
                file.length() != fileSize
            ) {
                return null; // File changed
            }
            return col;
        }

        void touch() {
            this.lastAccessed = System.currentTimeMillis();
        }

        long fileSize() {
            return fileSize;
        }

        long estimatedHeapWeight() {
            return estimatedHeapWeight;
        }

        Instant createdAt() {
            return createdAt;
        }

        long ageMillis() {
            return System.currentTimeMillis() - createdAt.toEpochMilli();
        }
    }
}
