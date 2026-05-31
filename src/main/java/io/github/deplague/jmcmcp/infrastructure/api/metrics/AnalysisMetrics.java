package io.github.deplague.jmcmcp.infrastructure.api.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * Central Micrometer metrics for JMC MCP Server analysis operations.
 *
 * <p>Provides timers, counters, gauges, and distribution summaries for:
 * <ul>
 *   <li>Analysis duration by type</li>
 *   <li>Active analysis count</li>
 *   <li>Cache hit/miss/eviction counters</li>
 *   <li>Async job queue depth</li>
 *   <li>Upload size distribution</li>
 * </ul>
 */
@Slf4j
@ApplicationScoped
public class AnalysisMetrics {

    private final MeterRegistry registry;

    // Active analysis gauge
    private final AtomicInteger activeAnalyses = new AtomicInteger(0);

    // Counters
    private final Counter cacheHitCounter;
    private final Counter cacheMissCounter;
    private final Counter cacheEvictionCounter;
    private final Counter analysisErrorCounter;

    @Inject
    public AnalysisMetrics(MeterRegistry registry) {
        this.registry = registry;

        // Gauge: active analyses
        Gauge.builder("jmc.analysis.active", activeAnalyses, AtomicInteger::get)
                .description("Number of analyses currently in progress")
                .register(registry);

        // Counter: cache hits
        this.cacheHitCounter = Counter.builder("jmc.cache.hit")
                .description("JFR recording cache hits")
                .register(registry);

        // Counter: cache misses
        this.cacheMissCounter = Counter.builder("jmc.cache.miss")
                .description("JFR recording cache misses")
                .register(registry);

        // Counter: cache evictions
        this.cacheEvictionCounter = Counter.builder("jmc.cache.eviction")
                .description("JFR recording cache evictions")
                .register(registry);

        // Counter: analysis errors
        this.analysisErrorCounter = Counter.builder("jmc.analysis.errors")
                .description("Total analysis errors")
                .register(registry);

        log.info("AnalysisMetrics initialized");
    }

    /**
     * Time an analysis operation and record metrics.
     *
     * @param analysisType the type of analysis (e.g., "hot-methods", "gc-detail")
     * @param operation    the operation to time
     * @param <T>          the result type
     * @return the operation result
     */
    public <T> T timeAnalysis(String analysisType, Supplier<T> operation) {
        activeAnalyses.incrementAndGet();
        Timer timer = Timer.builder("jmc.analysis.duration")
                .tag("type", analysisType)
                .description("Analysis operation duration")
                .register(registry);
        long start = System.nanoTime();
        try {
            return operation.get();
        } finally {
            activeAnalyses.decrementAndGet();
            timer.record(System.nanoTime() - start, java.util.concurrent.TimeUnit.NANOSECONDS);
        }
    }

    /**
     * Record a cache hit.
     */
    public void recordCacheHit() {
        cacheHitCounter.increment();
    }

    /**
     * Record a cache miss.
     */
    public void recordCacheMiss() {
        cacheMissCounter.increment();
    }

    /**
     * Record a cache eviction.
     */
    public void recordCacheEviction() {
        cacheEvictionCounter.increment();
    }

    /**
     * Record an analysis error.
     *
     * @param analysisType the analysis type that failed
     */
    public void recordAnalysisError(String analysisType) {
        analysisErrorCounter.increment();
        Counter.builder("jmc.analysis.errors")
                .tag("type", analysisType)
                .register(registry)
                .increment();
    }

    /**
     * Record the size of an uploaded JFR file.
     *
     * @param bytes the file size in bytes
     */
    public void recordUploadSize(long bytes) {
        DistributionSummary.builder("jmc.upload.size")
                .description("Uploaded JFR file size distribution")
                .baseUnit("bytes")
                .register(registry)
                .record(bytes);
    }

    /**
     * Register a gauge that tracks the current size of a cache.
     *
     * @param name       the metric name
     * @param description the metric description
     * @param supplier   the supplier for the current value
     */
    public void registerCacheGauge(String name, String description, Supplier<Number> supplier) {
        Gauge.builder(name, supplier)
                .description(description)
                .register(registry);
    }

    /**
     * Register async job queue depth gauge.
     *
     * @param supplier provides the current pending job count
     */
    public void registerJobQueueGauge(Supplier<Number> supplier) {
        Gauge.builder("jmc.jobs.pending", supplier)
                .description("Number of pending async analysis jobs")
                .register(registry);
    }
}
