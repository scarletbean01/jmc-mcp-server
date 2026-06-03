package io.github.deplague.jmcmcp.infrastructure.heapdump;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Weigher;
import io.github.deplague.jmcmcp.application.port.HeapDumpProvider;
import io.github.deplague.jmcmcp.domain.exception.HeapDumpParseException;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.netbeans.lib.profiler.heap.Heap;
import org.netbeans.lib.profiler.heap.HeapFactory;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@ApplicationScoped
public class HeapDumpCache implements HeapDumpProvider {

    private static final long DEFAULT_TTL_MINUTES =
            Long.getLong("jmc.heapdump.cache.ttl.minutes", 30);
    private static final double HEAP_MULTIPLIER_ESTIMATE =
            Double.parseDouble(System.getProperty("jmc.heapdump.cache.heap.multiplier", "5.0"));
    private static final int MAX_WEIGHT_PERCENT =
            Integer.getInteger("jmc.heapdump.cache.max-weight-percent", 30);

    private static final long MAX_CACHE_WEIGHT =
            Runtime.getRuntime().maxMemory() * Math.clamp(MAX_WEIGHT_PERCENT, 1, 100) / 100;

    private final ExecutorService parsingExecutor = Executors.newWorkStealingPool(
            Math.max(4, Runtime.getRuntime().availableProcessors())
    );

    private final Cache<String, CacheEntry> cache;

    public HeapDumpCache() {
        this.cache = Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofMinutes(DEFAULT_TTL_MINUTES))
                .maximumWeight(MAX_CACHE_WEIGHT)
                .weigher((Weigher<String, CacheEntry>) (_, value) ->
                        (int) Math.min(Integer.MAX_VALUE, value.estimatedHeapWeight))
                .removalListener((key, value, cause) -> {
                    if (value != null && value.estimatedHeapWeight > 512 * 1024 * 1024L) {
                        System.gc();
                    }
                    log.info("Evicted heap dump: {} (cause: {}, weight={})", key, cause,
                            value != null ? formatBytes(value.estimatedHeapWeight) : "?");
                })
                .recordStats()
                .build();
        log.info("HeapDumpCache initialized: TTL={}min, MaxWeight={}",
                DEFAULT_TTL_MINUTES, formatBytes(MAX_CACHE_WEIGHT));
    }

    @Override
    public Heap loadSnapshot(String filePath) throws IOException {
        File file = new File(filePath).getAbsoluteFile();
        String key = file.getAbsolutePath();

        CacheEntry entry = cache.getIfPresent(key);
        if (entry != null) {
            if (file.lastModified() == entry.fileLastModified && file.length() == entry.fileSize) {
                log.debug("Cache hit for heap dump: {}", key);
                return entry.heap;
            }
            cache.invalidate(key);
            log.info("Cache invalidated for heap dump (file changed): {}", key);
        }

        if (!file.exists() || !file.isFile()) {
            throw new HeapDumpParseException("Heap dump file does not exist: " + filePath);
        }

        log.info("Loading heap dump: {} (size={})", key, formatBytes(file.length()));

        Heap heap;
        try {
            heap = CompletableFuture.supplyAsync(() -> {
                try {
                    return HeapFactory.createHeap(file);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, parsingExecutor).join();
        } catch (Exception e) {
            throw new HeapDumpParseException("Failed to load heap dump: " + filePath, e);
        }

        CacheEntry newEntry = new CacheEntry(heap, file.lastModified(), file.length());
        cache.put(key, newEntry);

        log.info("Loaded heap dump into cache: {} (estWeight={})", key, formatBytes(newEntry.estimatedHeapWeight));
        return heap;
    }

    @Override
    public void evict(String filePath) {
        File file = new File(filePath).getAbsoluteFile();
        cache.invalidate(file.getAbsolutePath());
    }

    @Override
    public void clear() {
        cache.invalidateAll();
    }

    private static String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.2f %sB", bytes / Math.pow(1024, exp), pre);
    }

    private static final class CacheEntry {
        final Heap heap;
        final long fileLastModified;
        final long fileSize;
        final long estimatedHeapWeight;

        CacheEntry(Heap heap, long fileLastModified, long fileSize) {
            this.heap = heap;
            this.fileLastModified = fileLastModified;
            this.fileSize = fileSize;
            this.estimatedHeapWeight = (long) (fileSize * HEAP_MULTIPLIER_ESTIMATE);
        }
    }
}
