# JMC MCP Server — Performance Improvement Plan

**Version:** 1.0  
**Date:** 2026-05-31  
**Author:** Kimi (Performance Engineering Analysis)  
**Scope:** JVM tuning, Quarkus configuration, JFR analysis performance, virtual threads, async processing, memory management, caching, monitoring, and code-level optimizations.

---

## Executive Summary

The JMC MCP Server is a high-throughput, CPU-intensive application that parses large JFR recordings and exposes ~70 analysis tools via MCP and REST protocols. Current performance characteristics reveal several optimization opportunities:

- **JFR parsing** is CPU-bound and offloaded to a fixed platform thread pool, but the pool size is conservative.
- **Recording cache** uses Caffeine with weight-based eviction but lacks configurable TTL and refresh policies.
- **Virtual threads** are used pervasively (`@RunOnVirtualThread` on ~50 endpoints/tools), but carrier thread pool tuning is absent.
- **Memory pressure** from large `IItemCollection` objects is partially managed but heap estimation is heuristic-based.
- **Async job processing** uses raw `CompletableFuture.runAsync()` without a managed executor, risking thread starvation and unbounded concurrency.
- **No application-level metrics** (Micrometer/MicroProfile Metrics) are exposed for operational observability.
- **String-heavy Markdown formatting** in tool adapters generates significant short-lived garbage.

**Target outcomes:**
- Reduce JFR parsing latency by **30–50%** for recordings >100MB.
- Reduce peak heap usage during analysis by **25%** via improved caching and streaming.
- Achieve **p99 <2s** for synchronous analysis on 500MB recordings.
- Support **100+ concurrent virtual threads** without carrier thread pinning.

---

## 1. JVM Tuning

### 1.1 Garbage Collector Selection

**Current state:** Default G1GC (Java 25).  
**Recommendation:** Switch to **ZGC** for low-latency, or keep **G1GC** with optimized regions if heap >32GB.

**Rationale:** The workload is heap-heavy (caching parsed JFR data) with large allocation bursts during analysis. ZGC (generational in Java 25) provides sub-millisecond pause times and handles large heaps efficiently.

```bash
# ZGC (recommended for heaps 4GB–1TB)
-XX:+UseZGC
-XX:+ZGenerational
-XX:MaxGCPauseMillis=10

# OR G1GC tuned for throughput (if ZGC unavailable)
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-XX:G1HeapRegionSize=16m
-XX:+UseStringDeduplication
-XX:+UseLargePages
```

**Measurable target:** GC pause times <20ms (G1) or <5ms (ZGC) at p99.

### 1.2 Heap Sizing

**Current state:** No explicit heap sizing; relies on defaults.  
**Recommendation:** Set explicit min/max heap with headroom for off-heap JFR structures.

```bash
# Minimum 4GB, maximum 16GB (adjust based on deployment)
-Xms4g
-Xmx16g

# Metaspace
-XX:MaxMetaspaceSize=512m

# Direct memory for NIO (file uploads, SSE)
-XX:MaxDirectMemorySize=2g

# Code cache
-XX:ReservedCodeCacheSize=512m
-XX:+UseCodeCacheFlushing
```

**Rationale:** JFR `IItemCollection` objects can retain 4× the file size in heap. A 500MB JFR file may consume 2GB heap. With multiple concurrent analyses, 16GB provides safe headroom.

### 1.3 JIT Compiler Tuning

```bash
# Enable C2 compiler optimizations for hot analysis paths
-XX:+UseNUMA
-XX:+AlwaysPreTouch
-XX:+UseTransparentHugePages

# Tiered compilation thresholds (lower for faster warmup)
-XX:Tier3CompileThreshold=1000
-XX:Tier4CompileThreshold=5000

# Compile on first invocation for critical paths
-XX:CompileThreshold=1000
```

### 1.4 Virtual Thread JVM Options

```bash
# Enable virtual thread monitoring (Java 25)
-Djdk.virtualThreadScheduler.parallelism=32
-Djdk.virtualThreadScheduler.maxPoolSize=256

# Pinning diagnostics (dev/test only; remove in production)
-Djdk.tracePinnedThreads=full
```

**Measurable target:** Zero virtual thread pinning events in production logs.

---

## 2. Quarkus Configuration Optimizations

### 2.1 Current `application.properties` Gaps

The current config is minimal (22 lines). Expand with the following:

```properties
# ===================================================================
# Quarkus Core Tuning
# ===================================================================

# HTTP Layer
quarkus.http.port=8080
quarkus.http.root-path=/
quarkus.http.cors.enabled=true
quarkus.http.cors.origins=*
quarkus.http.cors.methods=GET,POST,DELETE,OPTIONS
quarkus.http.cors.headers=accept,authorization,content-type,x-requested-with

# File upload limits (keep current)
quarkus.http.limits.max-body-size=500M
quarkus.http.limits.max-form-attribute-size=500M

# Connection pooling
quarkus.http.limits.max-connections=1000
quarkus.http.idle-timeout=300s

# ===================================================================
# Thread Pool & Virtual Threads
# ===================================================================

# Virtual thread carrier pool (platform threads backing virtual threads)
quarkus.thread-pool.max-threads=1000
quarkus.thread-pool.core-threads=32
quarkus.thread-pool.queue-size=1000
quarkus.thread-pool.growth-resistance=0.0

# Keep-alive for platform threads
quarkus.thread-pool.keep-alive-time=60s

# ===================================================================
# Logging (strictly stderr for MCP stdio)
# ===================================================================

quarkus.log.console.stderr=true
quarkus.log.level=WARN
quarkus.log.category."io.github.deplague".level=INFO
quarkus.log.category."io.modelcontextprotocol".level=WARN
quarkus.log.category."org.openjdk.jmc".level=WARN

# Async logging to reduce blocking on virtual threads
quarkus.log.console.async=true
quarkus.log.console.async.queue-length=1024

# ===================================================================
# REST / JSON
# ===================================================================

# Jackson tuning for large responses
quarkus.jackson.write-dates-as-timestamps=false
quarkus.jackson.accept-single-value-as-array=true

# RESTEasy Reactive buffering
quarkus.resteasy-reactive.output-buffer-size=8192

# ===================================================================
# DevServices / Test (disable in production)
# ===================================================================
quarkus.devservices.enabled=false
```

### 2.2 Native Image Considerations

If building native images:

```properties
# Native image optimizations
quarkus.native.additional-build-args=-march=native,-O3
quarkus.native.enable-reports=true
quarkus.native.resources.includes=**.jfr
```

**Note:** JMC libraries use reflection heavily. Register JMC classes for reflection in `reflect-config.json`.

---

## 3. JFR Analysis Performance

### 3.1 Recording Cache Enhancements

**Current:** `JfrRecordingCache` uses Caffeine with weight-based eviction and a fixed 60-minute TTL.  
**Issues:**
- `HEAP_MULTIPLIER_ESTIMATE = 4.0` is a coarse heuristic.
- No refresh-after-write for frequently accessed recordings.
- No explicit memory pressure handling.
- `countEvents()` iterates all items on every load (O(N) cost).

**Optimizations:**

```java
// JfrRecordingCache.java — recommended changes

private static final long DEFAULT_TTL_MINUTES = 
    Long.getLong("jmc.cache.ttl.minutes", 60);
private static final long REFRESH_AFTER_WRITE_MINUTES = 
    Long.getLong("jmc.cache.refresh.minutes", 30);
private static final double HEAP_MULTIPLIER_ESTIMATE = 
    Double.parseDouble(System.getProperty("jmc.cache.heap.multiplier", "3.5"));

// Use bounded parser pool with work-stealing
private final ExecutorService parsingExecutor = Executors.newWorkStealingPool(
    Math.max(4, Runtime.getRuntime().availableProcessors())
);

public JfrRecordingCache(long ttlMinutes) {
    this.cache = Caffeine.newBuilder()
        .expireAfterAccess(Duration.ofMinutes(ttlMinutes))
        .refreshAfterWrite(Duration.ofMinutes(REFRESH_AFTER_WRITE_MINUTES))
        .maximumWeight(MAX_CACHE_WEIGHT)
        .weigher((Weigher<String, CacheEntry>) (key, value) -> 
            (int) Math.min(Integer.MAX_VALUE, value.estimatedHeapWeight))
        .removalListener((key, value, cause) -> {
            evictionCount.incrementAndGet();
            // Hint GC after large eviction
            if (value.estimatedHeapWeight > 512 * 1024 * 1024L) {
                System.gc();
            }
            LOG.info("Evicted recording: {} (cause: {}, weight={})", 
                key, cause, FormatUtil.formatBytes(value.estimatedHeapWeight));
        })
        .recordStats() // Enable Caffeine built-in stats
        .build();
}
```

**Configuration properties:**

```properties
# application.properties additions
jmc.cache.ttl.minutes=60
jmc.cache.refresh.minutes=30
jmc.cache.heap.multiplier=3.5
jmc.cache.max-weight-percent=50
```

**Measurable target:** Cache hit ratio >85% for repeated analysis requests.

### 3.2 Streaming & Lazy Evaluation

**Current:** Many services iterate `IItemCollection` eagerly, materializing all results.  
**Recommendation:** Use lazy `Stream` pipelines and short-circuit where possible.

**Example — `HotMethodsService`:**

```java
// BEFORE: Materializes entire HashMap
Map<StackTraceKey, Long> traceCounts = new HashMap<>();
for (IItemIterable iterable : samples) { ... }

// AFTER: Use parallel streams with custom spliterator (if thread-safe)
// Or: Limit iteration with early termination
List<HotMethodEntry> entries = new ArrayList<>(topN);
// Use a priority queue (min-heap) to track top-N without full sort
PriorityQueue<Map.Entry<StackTraceKey, Long>> topNHeap = 
    new PriorityQueue<>(topN, Map.Entry.comparingByValue());
// ... populate heap, then extract sorted result
```

### 3.3 Batch Statistics Optimization

**Current:** `JfrQuantityAggregator.batchStats()` allocates an `ArrayList<IQuantity>` for percentiles.  
**Optimization:** Use primitive `DoubleArrayList` or `TDoubleArrayList` (Trove) to avoid boxing.

```java
// Add to pom.xml:
// <dependency>
//   <groupId>net.sf.trove4j</groupId>
//   <artifactId>trove4j</artifactId>
//   <version>3.0.3</version>
// </dependency>

// In batchStats():
TDoubleArrayList values = new TDoubleArrayList();
// ... add q.doubleValue() instead of boxing IQuantity
values.sort();
// Compute percentile via direct array access
```

**Measurable target:** Reduce `batchStats()` allocation by **40%**.

---

## 4. Virtual Thread Optimizations

### 4.1 Current Usage Analysis

- **~50 MCP tools** and **~5 REST endpoints** use `@RunOnVirtualThread`.
- **JFR parsing** is explicitly offloaded to `Executors.newFixedThreadPool()` to avoid pinning.
- **Async jobs** use `CompletableFuture.runAsync()` without an executor → falls back to `ForkJoinPool.commonPool()`.

### 4.2 Recommendations

#### A. Managed Async Executor

Replace raw `CompletableFuture.runAsync()` with a named, bounded executor:

```java
// AsyncJobService.java
@ApplicationScoped
public class AsyncJobService {
    
    private final ExecutorService analysisExecutor;
    
    public AsyncJobService() {
        this.analysisExecutor = Executors.newVirtualThreadPerTaskExecutor();
        // OR for CPU-bound work:
        // this.analysisExecutor = Executors.newFixedThreadPool(
        //     Runtime.getRuntime().availableProcessors(),
        //     Thread.ofVirtual().factory()
        // );
    }
    
    public AsyncJob createJob(String recordingId, String analysisType) {
        AsyncJob job = createJobInternal(recordingId, analysisType);
        
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                jobService.updateJobStatus(job.jobId(), "RUNNING", 10);
                Object result = dispatcher.dispatch(analysisType, filePath, request);
                jobService.completeJob(job.jobId(), result);
            } catch (Exception e) {
                jobService.failJob(job.jobId(), e.getMessage());
            }
        }, analysisExecutor);
        
        jobService.setJobFuture(job.jobId(), future);
        return job;
    }
}
```

#### B. Avoid Pinning in Domain Services

**Current risk:** `synchronized` blocks or `ReentrantLock` usage inside virtual threads can pin carriers.

**Audit checklist:**
1. Search for `synchronized` keywords in domain services.
2. Replace with `java.util.concurrent.locks.ReentrantLock` (non-pinning in Java 25).
3. Ensure no native code or `Object.wait()` inside virtual threads.

```java
// BEFORE (pinning risk):
synchronized (this) { ... }

// AFTER (virtual-thread safe):
private final ReentrantLock lock = new ReentrantLock();
lock.lock();
try { ... } finally { lock.unlock(); }
```

#### C. Carrier Thread Pool Tuning

```properties
# JVM system properties (not Quarkus)
-Djdk.virtualThreadScheduler.parallelism=32
-Djdk.virtualThreadScheduler.maxPoolSize=256
```

**Recommendation:** Set `parallelism` to the number of physical cores, `maxPoolSize` to 4×–8× for I/O-bound work.

---

## 5. Async Processing Improvements

### 5.1 Job Queue with Backpressure

**Current:** Unlimited concurrent async jobs via `CompletableFuture`.  
**Risk:** Memory exhaustion under burst load.

**Solution:** Implement a bounded job semaphore.

```java
@ApplicationScoped
public class AsyncJobService {
    
    private static final int MAX_CONCURRENT_JOBS = 
        Integer.getInteger("jmc.jobs.max-concurrent", 20);
    
    private final Semaphore jobSemaphore = new Semaphore(MAX_CONCURRENT_JOBS);
    
    public AsyncJob submitJob(String recordingId, String analysisType, 
                              AnalysisRequest request, String filePath) {
        AsyncJob job = createJob(recordingId, analysisType);
        
        CompletableFuture.runAsync(() -> {
            boolean acquired = false;
            try {
                acquired = jobSemaphore.tryAcquire(30, TimeUnit.SECONDS);
                if (!acquired) {
                    failJob(job.jobId(), "Server overloaded — job queue full");
                    return;
                }
                updateJobStatus(job.jobId(), "RUNNING", 10);
                Object result = dispatcher.dispatch(analysisType, filePath, request);
                completeJob(job.jobId(), result);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                failJob(job.jobId(), "Job interrupted");
            } catch (Exception e) {
                failJob(job.jobId(), e.getMessage());
            } finally {
                if (acquired) jobSemaphore.release();
            }
        }, analysisExecutor);
        
        return job;
    }
}
```

**Configuration:**

```properties
jmc.jobs.max-concurrent=20
jmc.jobs.queue-timeout-seconds=30
```

### 5.2 Progress Reporting Granularity

**Current:** Progress jumps from 10% → 100%.  
**Improvement:** Add fine-grained progress for long-running analyses.

```java
// In AnalysisDispatcher or individual services
public interface ProgressReporter {
    void report(int percent, String stage);
}

// Pass ProgressReporter through service calls for multi-stage analyses
```

---

## 6. Memory Management for Large JFR Files

### 6.1 Off-Heap Streaming for Uploads

**Current:** `Files.copy(fileData, targetPath)` streams through heap.  
**Optimization:** Use `FileChannel.transferFrom()` for zero-copy uploads.

```java
// RecordingStorageService.java
public UploadResponse storeRecording(String fileName, InputStream fileData, long fileSize) 
        throws IOException {
    String recordingId = UUID.randomUUID().toString();
    Path targetPath = uploadDir.resolve(recordingId + ".jfr");
    
    try (var out = FileChannel.open(targetPath, 
            StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
        // For FileInputStream, use transferFrom directly
        if (fileData instanceof FileInputStream fis) {
            out.transferFrom(fis.getChannel(), 0, Long.MAX_VALUE);
        } else {
            // Fallback to buffered copy with large buffer
            byte[] buffer = new byte[8192 * 16]; // 128KB buffer
            int read;
            long position = 0;
            while ((read = fileData.read(buffer)) > 0) {
                out.write(ByteBuffer.wrap(buffer, 0, read), position);
                position += read;
            }
        }
    }
    
    // Pre-load into JFR cache to validate
    IItemCollection events = jfrProvider.loadRecording(targetPath.toString());
    // ... rest unchanged
}
```

### 6.2 Recording Lifecycle Management

**Current:** 24-hour TTL with hourly cleanup.  
**Enhancement:** Add size-based eviction and immediate cleanup on memory pressure.

```java
// RecordingStorageService.java
@Scheduled(every = "1h")
void cleanupExpiredRecordings() {
    Instant cutoff = Instant.now().minus(MAX_AGE);
    long totalSize = recordings.values().stream()
        .mapToLong(m -> m.fileSize)
        .sum();
    long maxTotalSize = Long.getLong("jmc.storage.max-total-size", 
        50L * 1024 * 1024 * 1024); // 50GB default
    
    // Sort by age, evict oldest if over size limit
    List<Map.Entry<String, RecordingMetadata>> sorted = 
        new ArrayList<>(recordings.entrySet());
    sorted.sort(Comparator.comparing(e -> e.getValue().uploadedAt));
    
    for (Map.Entry<String, RecordingMetadata> entry : sorted) {
        RecordingMetadata meta = entry.getValue();
        boolean expired = meta.uploadedAt.isBefore(cutoff);
        boolean overSize = totalSize > maxTotalSize;
        
        if (expired || overSize) {
            try {
                Files.deleteIfExists(meta.filePath);
                cache.evict(meta.filePath.toString()); // Also evict from JFR cache
                totalSize -= meta.fileSize;
                log.info("Cleaned up recording {} (expired={}, overSize={})", 
                    meta.recordingId, expired, overSize);
            } catch (IOException e) {
                log.warn("Failed to delete recording {}", meta.recordingId, e);
            }
            recordings.remove(entry.getKey());
        }
        if (!overSize && !expired) break;
    }
}
```

**Configuration:**

```properties
jmc.storage.max-total-size=53687091200
jmc.storage.max-age-hours=24
```

### 6.3 Soft/Weak References for Large Objects

For `StacktraceTreeModel` in `CallTreeCache`, consider `SoftReference` wrappers to allow GC under memory pressure:

```java
// CallTreeCache.java
private final Cache<String, SoftReference<CachedTree>> trees;

public CachedTree getTree(String treeId) {
    SoftReference<CachedTree> ref = trees.getIfPresent(treeId);
    if (ref != null) {
        CachedTree tree = ref.get();
        if (tree != null) return tree;
        trees.invalidate(treeId); // Clean up dead reference
    }
    return null;
}
```

---

## 7. Database / Cache Tuning

### 7.1 Caffeine Cache Metrics

Enable Caffeine statistics and expose via Micrometer:

```java
// JfrRecordingCache.java
.cacheStats() // returns CacheStats with hitRate, evictionCount, etc.

// HealthCheckApplicationService.java
public HealthCheckReport check() {
    CacheStats stats = recordingCache.getCacheStats();
    // Include hitRate, loadCount, evictionWeight in report
}
```

### 7.2 Call Tree Cache Tuning

**Current:** Fixed `MAX_ENTRIES = 50`.  
**Recommendation:** Make configurable and add weight-based eviction.

```java
// CallTreeCache.java
private static final int MAX_ENTRIES = 
    Integer.getInteger("jmc.calltree.max-entries", 50);
private static final long MAX_WEIGHT = 
    Long.getLong("jmc.calltree.max-weight", 2L * 1024 * 1024 * 1024); // 2GB
```

### 7.3 Result Caching (Application Layer)

**New:** Cache expensive analysis results (e.g., `compareRecordings`, `quickAnalysis`) keyed by `(recordingId, analysisType, paramsHash)`.

```java
@ApplicationScoped
public class AnalysisResultCache {
    private final Cache<String, Object> resultCache = Caffeine.newBuilder()
        .expireAfterWrite(Duration.ofMinutes(15))
        .maximumSize(200)
        .build();
    
    public Object getOrCompute(String key, Supplier<Object> supplier) {
        return resultCache.get(key, k -> supplier.get());
    }
}
```

**Use case:** Repeated `quick-analysis` or `health-check` calls on the same recording.

---

## 8. Monitoring and Observability

### 8.1 Add Micrometer / MicroProfile Metrics

**Add to `pom.xml`:**

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-micrometer-registry-prometheus</artifactId>
</dependency>
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-health</artifactId>
</dependency>
```

**Custom metrics to instrument:**

| Metric Name | Type | Description |
|-------------|------|-------------|
| `jmc.analysis.duration` | Timer | Analysis execution time by type |
| `jmc.analysis.active` | Gauge | Currently running analyses |
| `jmc.cache.hit` | Counter | JFR recording cache hits |
| `jfr.cache.miss` | Counter | JFR recording cache misses |
| `jmc.cache.eviction` | Counter | Cache evictions |
| `jmc.jobs.pending` | Gauge | Pending async jobs |
| `jmc.jobs.completed` | Counter | Completed async jobs |
| `jmc.upload.size` | DistributionSummary | Uploaded file sizes |
| `jmc.heap.used_after_analysis` | Gauge | Heap after major analysis |

**Example instrumentation:**

```java
@ApplicationScoped
public class AnalysisMetrics {
    private final MeterRegistry registry;
    private final AtomicInteger activeAnalyses = new AtomicInteger(0);
    
    public AnalysisMetrics(MeterRegistry registry) {
        this.registry = registry;
        Gauge.builder("jmc.analysis.active", activeAnalyses, AtomicInteger::get)
            .register(registry);
    }
    
    public <T> T timed(String analysisType, Supplier<T> supplier) {
        activeAnalyses.incrementAndGet();
        try {
            return Timer.builder("jmc.analysis.duration")
                .tag("type", analysisType)
                .register(registry)
                .record(supplier);
        } finally {
            activeAnalyses.decrementAndGet();
        }
    }
}
```

### 8.2 Health Checks

**Add liveness/readiness probes:**

```java
@Liveness
@ApplicationScoped
public class JmcLivenessCheck implements HealthCheck {
    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.up("jmc-server-live");
    }
}

@Readiness
@ApplicationScoped
public class JmcReadinessCheck implements HealthCheck {
    @Inject JfrRecordingCache cache;
    
    @Override
    public HealthCheckResponse call() {
        boolean healthy = cache.size() >= 0; // Basic sanity
        return HealthCheckResponse.builder("jmc-server-ready")
            .status(healthy)
            .withData("cacheSize", cache.size())
            .build();
    }
}
```

**Endpoints:**
- `/q/health/live`
- `/q/health/ready`
- `/q/metrics` (Prometheus format)

### 8.3 Distributed Tracing

```xml
<!-- pom.xml -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-opentelemetry</artifactId>
</dependency>
```

```properties
# application.properties
quarkus.otel.enabled=true
quarkus.otel.exporter.otlp.traces.endpoint=http://localhost:4317
quarkus.otel.service.name=jmc-mcp-server
quarkus.otel.resource.attributes=deployment.production
```

**Trace spans:**
- `jfr.parse` — JFR file parsing
- `jfr.analysis` — Analysis execution
- `jfr.cache.load` — Cache load operations

---

## 9. Code-Level Optimizations

### 9.1 StringBuilder Pooling in Markdown Formatters

**Current:** Every tool creates new `StringBuilder` instances.  
**Optimization:** Use `ThreadLocal<StringBuilder>` for per-thread reuse.

```java
public final class MarkdownBuffer {
    private static final ThreadLocal<StringBuilder> BUFFER = 
        ThreadLocal.withInitial(() -> new StringBuilder(4096));
    
    public static StringBuilder acquire() {
        StringBuilder sb = BUFFER.get();
        sb.setLength(0);
        sb.trimToSize(); // Optional: prevent unbounded growth
        return sb;
    }
    
    public static String releaseAndGet(StringBuilder sb) {
        String result = sb.toString();
        sb.setLength(0);
        return result;
    }
}

// Usage in tool:
StringBuilder sb = MarkdownBuffer.acquire();
sb.append("# Results\n\n");
// ... build markdown
return ToolResponse.success(MarkdownBuffer.releaseAndGet(sb));
```

### 9.2 Reduce Boxed Collections in Aggregators

**Current:** `JfrQuantityAggregator` uses `List<IQuantity>` for percentiles.  
**Optimization:** Use primitive arrays for intermediate values.

```java
public static IQuantity percentileQuantity(IItemCollection items, String identifier, double percentile) {
    // Use TDoubleArrayList or double[] with manual resizing
    double[] values = new double[1024];
    int count = 0;
    
    for (IItemIterable iterable : items) {
        IMemberAccessor<Object, IItem> accessor = JfrAccessorRepository.getAccessor(iterable.getType(), identifier);
        if (accessor != null) {
            for (IItem item : iterable) {
                Object raw = accessor.getMember(item);
                if (raw != null) {
                    IQuantity q = JfrValueConverter.toIQuantity(raw);
                    if (q != null) {
                        if (count == values.length) {
                            values = Arrays.copyOf(values, values.length * 2);
                        }
                        values[count++] = q.doubleValue();
                    }
                }
            }
        }
    }
    
    if (count == 0) return null;
    Arrays.sort(values, 0, count);
    int index = (int) Math.max(0, Math.ceil((percentile / 100.0) * count) - 1);
    // Need unit for reconstruction — track separately
    return unit != null ? unit.quantity(values[index]) : null;
}
```

### 9.3 Parallel Analysis in CompareRecordingsService

**Current:** Already uses `CompletableFuture.supplyAsync()` but without a custom executor.  **Enhancement:** Use `ForkJoinPool.commonPool()` with parallelism tuning, or a dedicated executor.

```java
// CompareRecordingsService.java
private static final ExecutorService COMPARE_EXECUTOR = Executors.newWorkStealingPool(
    Runtime.getRuntime().availableProcessors()
);

// In analyze():
CompletableFuture<Map<String, List<String>>> metricsTask =
    supplyAsync(() -> calculateMetricsMarkdown(bCtx, tCtx), COMPARE_EXECUTOR);
```

### 9.4 Lazy IItemCollection Filtering

**Current:** `JfrProviderImpl.filterByTimeRange()` eagerly applies filters.  **Optimization:** Cache filtered views for repeated time-range queries.

```java
// JfrProviderImpl.java
private final Cache<String, IItemCollection> filteredCache = Caffeine.newBuilder()
    .expireAfterWrite(Duration.ofMinutes(10))
    .maximumSize(100)
    .build();

public IItemCollection filterByTimeRange(IItemCollection events, String start, String end) {
    String key = events.hashCode() + "|" + start + "|" + end;
    return filteredCache.get(key, k -> doFilter(events, start, end));
}
```

### 9.5 StackTraceKey Hash Caching

**Current:** `StackTraceKey.hashCode()` computes on first call and caches.  **Status:** Already optimized.  **Enhancement:** Pre-compute hash during construction if stack trace is immutable (JMC frames are).

### 9.6 Avoid Redundant Type Lookups

**Current:** Many services call `iterable.getType()` multiple times per loop.  **Fix:** Extract to local variable.

```java
// BEFORE:
IMemberAccessor<Object, IItem> stackAccessor = getAccessor(iterable.getType(), "stackTrace");
IType<?> type = iterable.getType(); // Redundant
IMemberAccessor<Object, IItem> threadAccessor = getAccessor(type, "sampledThread");

// AFTER:
IType<?> type = iterable.getType();
IMemberAccessor<Object, IItem> stackAccessor = getAccessor(type, "stackTrace");
IMemberAccessor<Object, IItem> threadAccessor = getAccessor(type, "sampledThread");
```

---

## 10. Benchmarking Strategy

### 10.1 JMH Micro-Benchmarks

**Add to `pom.xml`:**

```xml
<dependency>
    <groupId>org.openjdk.jmh</groupId>
    <artifactId>jmh-core</artifactId>
    <version>1.37</version>
    <scope>test</scope>
</dependency>
```

**Benchmark classes to create:**

```java
// src/test/java/.../benchmark/JfrParsingBenchmark.java
@State(Scope.Benchmark)
public class JfrParsingBenchmark {
    private Path jfrFile;
    private JfrRecordingCache cache;
    
    @Setup
    public void setup() {
        jfrFile = Paths.get("src/test/resources/large-recording.jfr");
        cache = new JfrRecordingCache(10);
    }
    
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(2)
    @Warmup(iterations = 3)
    @Measurement(iterations = 5)
    public IItemCollection benchmarkParse() throws IOException {
        return cache.load(jfrFile.toString());
    }
}
```

**Benchmark targets:**

| Benchmark | Baseline | Target |
|-----------|----------|--------|
| `JfrParsingBenchmark` | — | <3s for 500MB file |
| `HotMethodsBenchmark` | — | <500ms for 1M events |
| `BatchStatsBenchmark` | — | <200ms for 500K quantities |
| `StackTraceKeyBenchmark` | — | <50ns per hash/equals |

### 10.2 Load Testing with k6

**Script:** `benchmarks/k6-load-test.js`

```javascript
import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  stages: [
    { duration: '2m', target: 10 },   // Ramp up
    { duration: '5m', target: 50 },   // Steady state
    { duration: '2m', target: 100 },  // Stress
    { duration: '2m', target: 0 },    // Ramp down
  ],
  thresholds: {
    http_req_duration: ['p(95)<2000'], // 2s p95
    http_req_failed: ['rate<0.01'],    // <1% errors
  },
};

export default function () {
  const res = http.post('http://localhost:8080/api/v1/recordings/test/analyze/overview');
  check(res, {
    'status is 200': (r) => r.status === 200,
    'response time < 2s': (r) => r.timings.duration < 2000,
  });
  sleep(1);
}
```

**Run:**
```bash
k6 run benchmarks/k6-load-test.js
```

### 10.3 Continuous Profiling

**Tools:**
- **async-profiler:** For CPU and allocation profiling during load tests.
- **JFR self-profiling:** Enable JFR on the server itself to analyze its own performance.

```bash
# Start server with self-profiling
java -XX:StartFlightRecording=settings=profile,filename=server.jfr \
     -jar target/jmc-mcp-1.0.0-SNAPSHOT-runner.jar
```

### 10.4 Regression Testing

**CI Integration:**

```yaml
# .github/workflows/perf.yml (example)
name: Performance Regression
on: [pull_request]
jobs:
  benchmark:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '25'
          distribution: 'temurin'
      - run: mvn test -Pbenchmark
      - run: |
          java -jar target/benchmarks.jar > benchmark-results.txt
          # Compare against baseline and fail if regression >10%
```

---

## Implementation Roadmap

### Phase 1: Quick Wins (Week 1)
- [ ] Add JVM tuning flags (`-Xmx`, GC selection, virtual thread options).
- [ ] Expand `application.properties` with thread pool and HTTP tuning.
- [ ] Replace `CompletableFuture.runAsync()` with managed executor in `AsyncJobService`.
- [ ] Add backpressure semaphore for async jobs.
- [ ] Fix redundant `iterable.getType()` calls in hot services.

### Phase 2: Caching & Memory (Week 2)
- [ ] Implement `refreshAfterWrite` and configurable TTL in `JfrRecordingCache`.
- [ ] Add size-based storage eviction in `RecordingStorageService`.
- [ ] Add `AnalysisResultCache` for expensive analyses.
- [ ] Evaluate `SoftReference` for `CallTreeCache`.

### Phase 3: Observability (Week 3)
- [ ] Add `quarkus-micrometer-registry-prometheus` dependency.
- [ ] Instrument `AnalysisDispatcher` with timers and gauges.
- [ ] Add custom health checks (`JmcLivenessCheck`, `JmcReadinessCheck`).
- [ ] Add OpenTelemetry tracing for JFR operations.

### Phase 4: Code Optimization (Week 4)
- [ ] Implement `MarkdownBuffer` `ThreadLocal` pooling.
- [ ] Optimize `JfrQuantityAggregator` with primitive arrays.
- [ ] Add `PriorityQueue` top-N to `HotMethodsService`.
- [ ] Add zero-copy upload via `FileChannel`.

### Phase 5: Benchmarking & Validation (Week 5)
- [ ] Create JMH benchmark suite.
- [ ] Run k6 load tests and establish baselines.
- [ ] Profile with async-profiler and fix hotspots.
- [ ] Document final performance numbers and update this plan.

---

## Appendix A: Recommended `application.properties` (Full)

```properties
# ===================================================================
# JMC MCP Server — Production Configuration
# ===================================================================

# HTTP
quarkus.http.port=8080
quarkus.http.root-path=/
quarkus.http.cors.enabled=true
quarkus.http.cors.origins=*
quarkus.http.cors.methods=GET,POST,DELETE,OPTIONS
quarkus.http.cors.headers=accept,authorization,content-type,x-requested-with
quarkus.http.limits.max-body-size=500M
quarkus.http.limits.max-form-attribute-size=500M
quarkus.http.limits.max-connections=1000
quarkus.http.idle-timeout=300s

# Thread Pool
quarkus.thread-pool.max-threads=1000
quarkus.thread-pool.core-threads=32
quarkus.thread-pool.queue-size=1000
quarkus.thread-pool.growth-resistance=0.0
quarkus.thread-pool.keep-alive-time=60s

# Logging
quarkus.log.console.stderr=true
quarkus.log.level=WARN
quarkus.log.category."io.github.deplague".level=INFO
quarkus.log.category."io.modelcontextprotocol".level=WARN
quarkus.log.category."org.openjdk.jmc".level=WARN
quarkus.log.console.async=true
quarkus.log.console.async.queue-length=1024

# Jackson
quarkus.jackson.write-dates-as-timestamps=false
quarkus.jackson.accept-single-value-as-array=true
quarkus.resteasy-reactive.output-buffer-size=8192

# Storage
storage.path=uploads
jmc.storage.max-total-size=53687091200
jmc.storage.max-age-hours=24

# Cache Tuning
jmc.cache.ttl.minutes=60
jmc.cache.refresh.minutes=30
jmc.cache.heap.multiplier=3.5
jmc.cache.max-weight-percent=50
jmc.calltree.max-entries=50
jmc.calltree.max-weight=2147483648

# Async Jobs
jmc.jobs.max-concurrent=20
jmc.jobs.queue-timeout-seconds=30

# Metrics
quarkus.micrometer.enabled=true
quarkus.micrometer.export.prometheus.enabled=true
quarkus.micrometer.registry-enabled-default=true

# Health
quarkus.smallrye-health.enabled=true

# Tracing
quarkus.otel.enabled=true
quarkus.otel.service.name=jmc-mcp-server

# DevServices
quarkus.devservices.enabled=false
```

## Appendix B: Recommended JVM Launch Flags

```bash
java \
  -Xms4g -Xmx16g \
  -XX:MaxMetaspaceSize=512m \
  -XX:MaxDirectMemorySize=2g \
  -XX:ReservedCodeCacheSize=512m \
  -XX:+UseZGC -XX:+ZGenerational \
  -XX:MaxGCPauseMillis=10 \
  -XX:+UseNUMA -XX:+AlwaysPreTouch \
  -XX:+UseTransparentHugePages \
  -XX:+UseStringDeduplication \
  -Djdk.virtualThreadScheduler.parallelism=32 \
  -Djdk.virtualThreadScheduler.maxPoolSize=256 \
  -jar target/jmc-mcp-1.0.0-SNAPSHOT-runner.jar
```

## Appendix C: Performance Checklist

Before each release, verify:

- [ ] JMH benchmarks show no regression >10%.
- [ ] k6 load test passes with p95 <2s and error rate <1%.
- [ ] async-profiler shows no hotspots in `StringBuilder.append` or `IQuantity` boxing.
- [ ] Heap dump analysis shows no leak in `JfrRecordingCache` or `CallTreeCache`.
- [ ] Virtual thread pinning diagnostics show zero pinning events.
- [ ] Prometheus metrics show cache hit ratio >85%.
- [ ] GC logs show pause times within target (ZGC <5ms, G1 <20ms).

---

*End of Performance Improvement Plan*
