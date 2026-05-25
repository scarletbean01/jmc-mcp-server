package io.github.deplague.jmcmcp.jfr;

import io.github.deplague.jmcmcp.async.AsyncJobService;
import io.github.deplague.jmcmcp.security.RecordingAccessController;
import org.openjdk.jmc.common.IDisplayable;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;
import org.openjdk.jmc.flightrecorder.rules.util.RulesToolkit;
import org.openjdk.jmc.flightrecorder.rules.util.RulesToolkit.EventAvailability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Core service for loading JFR recordings, caching results, validating paths,
 * and executing analysis tasks (sync or async).
 */
public final class JfrAnalysisService {

    private static final Logger LOG = LoggerFactory.getLogger(JfrAnalysisService.class);
    private static final int MAX_RESULT_CACHE_ENTRIES = 100;
    private static final long RESULT_CACHE_TTL_MINUTES = 60 * 24; // 24 hours

    private final JfrRecordingCache cache;
    private final RecordingAccessController accessController;
    private final AsyncJobService asyncJobService;

    private final Map<String, ResultCacheEntry> resultCache =
            Collections.synchronizedMap(new LinkedHashMap<>(MAX_RESULT_CACHE_ENTRIES, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, ResultCacheEntry> eldest) {
                    return size() > MAX_RESULT_CACHE_ENTRIES;
                }
            });

    private final AtomicLong resultHitCount = new AtomicLong(0);
    private final AtomicLong resultMissCount = new AtomicLong(0);

    public JfrAnalysisService(JfrRecordingCache cache) {
        this(cache, new RecordingAccessController(), new AsyncJobService());
    }

    public JfrAnalysisService(JfrRecordingCache cache,
                              RecordingAccessController accessController,
                              AsyncJobService asyncJobService) {
        this.cache = cache;
        this.accessController = accessController;
        this.asyncJobService = asyncJobService;
    }

    // ------------------------------------------------------------------
    // Path validation
    // ------------------------------------------------------------------

    /**
     * Validate that a recording path is allowed by the access controller.
     */
    public void validatePath(String filePath) {
        accessController.validate(filePath);
    }

    // ------------------------------------------------------------------
    // Recording loading
    // ------------------------------------------------------------------

    /**
     * Load (or retrieve from cache) the events for the given recording path.
     * Validates the path before loading.
     */
    public IItemCollection loadRecording(String filePath) throws IOException {
        validatePath(filePath);
        return cache.load(filePath);
    }

    // ------------------------------------------------------------------
    // Time-range filtering
    // ------------------------------------------------------------------

    /**
     * Filter the event collection by an optional time range.
     */
    public IItemCollection filterByTimeRange(IItemCollection events, String startTimeStr, String endTimeStr) {
        if (startTimeStr == null && endTimeStr == null) {
            return events;
        }

        IQuantity start = null;
        IQuantity end = null;

        if (startTimeStr != null) {
            try {
                Instant instant = Instant.parse(startTimeStr);
                long epochNanos = instant.getEpochSecond() * 1_000_000_000L + instant.getNano();
                start = UnitLookup.EPOCH_NS.quantity(epochNanos);
            } catch (Exception e) {
                LOG.warn("Failed to parse start_time: {}", startTimeStr);
            }
        }

        if (endTimeStr != null) {
            try {
                Instant instant = Instant.parse(endTimeStr);
                long epochNanos = instant.getEpochSecond() * 1_000_000_000L + instant.getNano();
                end = UnitLookup.EPOCH_NS.quantity(epochNanos);
            } catch (Exception e) {
                LOG.warn("Failed to parse end_time: {}", endTimeStr);
            }
        }

        if (start != null && end != null) {
            return events.apply(ItemFilters.interval(JfrAttributes.START_TIME, start, true, end, true));
        } else if (start != null) {
            return events.apply(ItemFilters.moreOrEqual(JfrAttributes.START_TIME, start));
        } else if (end != null) {
            return events.apply(ItemFilters.lessOrEqual(JfrAttributes.END_TIME, end));
        }

        return events;
    }

    // ------------------------------------------------------------------
    // Result caching with TTL
    // ------------------------------------------------------------------

    /**
     * Get a cached analysis result if available and not expired.
     */
    public String getCachedResult(String filePath, String toolName, Map<String, Object> args) {
        String key = buildCacheKey(filePath, toolName, args);
        ResultCacheEntry entry = resultCache.get(key);
        if (entry != null && !entry.isExpired()) {
            resultHitCount.incrementAndGet();
            LOG.debug("Result cache hit for {}: {}", toolName, filePath);
            return entry.result();
        }
        if (entry != null && entry.isExpired()) {
            resultCache.remove(key);
        }
        return null;
    }

    /**
     * Cache an analysis result with TTL.
     */
    public void cacheResult(String filePath, String toolName, Map<String, Object> args, String result) {
        String key = buildCacheKey(filePath, toolName, args);
        resultCache.put(key, new ResultCacheEntry(result));
    }

    public long getResultHitCount() {
        return resultHitCount.get();
    }

    public long getResultMissCount() {
        return resultMissCount.get();
    }

    public int getResultCacheSize() {
        return resultCache.size();
    }

    private String buildCacheKey(String filePath, String toolName, Map<String, Object> args) {
        // Use a stable hash of args to keep key size reasonable
        return filePath + ":" + toolName + ":" + (args != null ? args.hashCode() : 0);
    }

    // ------------------------------------------------------------------
    // Async execution
    // ------------------------------------------------------------------

    /**
     * Execute a tool analysis, either synchronously or asynchronously.
     *
     * @param toolName   the MCP tool name
     * @param args       the tool arguments (may contain "async" flag)
     * @param analyzer   the analysis logic
     * @return CallToolResult — either the direct result or a job submission confirmation
     */
    public CallToolResult execute(String toolName, Map<String, Object> args, Callable<String> analyzer) {
        boolean async = SchemaUtil.getBooleanOrDefault(args, "async", false);

        if (async) {
            resultMissCount.incrementAndGet();
            String jobId = asyncJobService.submit(toolName, args, analyzer);
            String msg = String.format(
                    "# Async Job Submitted\n\n" +
                    "- **Job ID:** `%s`\n" +
                    "- **Tool:** `%s`\n" +
                    "- **Status:** PENDING\n\n" +
                    "Use `get_job_status` to check progress and `get_job_result` to retrieve the output.",
                    jobId, toolName);
            return CallToolResult.builder().addTextContent(msg).isError(false).build();
        }

        // Synchronous path — check cache first
        String filePath = SchemaUtil.getStringOrDefault(args, "jfr_file_path",
                SchemaUtil.getStringOrDefault(args, "baseline_jfr_path", null));
        if (filePath == null) {
            filePath = SchemaUtil.getStringOrDefault(args, "target_jfr_path", "");
        }

        String cached = getCachedResult(filePath, toolName, args);
        if (cached != null) {
            return CallToolResult.builder().addTextContent(cached).isError(false).build();
        }

        try {
            String result = analyzer.call();
            cacheResult(filePath, toolName, args, result);
            return CallToolResult.builder().addTextContent(result).isError(false).build();
        } catch (Exception e) {
            return CallToolResult.builder()
                    .addTextContent("Error: " + e.getMessage())
                    .isError(true)
                    .build();
        }
    }

    public AsyncJobService getAsyncJobService() {
        return asyncJobService;
    }

    public JfrRecordingCache getRecordingCache() {
        return cache;
    }

    // ------------------------------------------------------------------
    // Recording overview
    // ------------------------------------------------------------------

    /**
     * Build a high-level overview of the recording.
     */
    public RecordingOverview getOverview(String filePath) throws IOException {
        IItemCollection events = loadRecording(filePath);

        double durationSeconds = 0;
        IQuantity startTime = RulesToolkit.getEarliestStartTime(events);
        IQuantity endTime = RulesToolkit.getLatestEndTime(events);
        if (startTime != null && endTime != null) {
            durationSeconds = endTime.subtract(startTime).doubleValue();
        }

        Map<String, Long> eventCounts = new HashMap<>();
        Map<String, EventAvailability> availability = new HashMap<>();

        for (IItemIterable itemIterable : events) {
            String typeId = itemIterable.getType().getIdentifier();
            long count = itemIterable.stream().count();
            eventCounts.merge(typeId, count, Long::sum);

            EventAvailability avail = RulesToolkit.getEventAvailability(events, typeId);
            if (avail != null) {
                availability.put(typeId, avail);
            }
        }

        return new RecordingOverview(filePath, durationSeconds, eventCounts, availability);
    }

    // ------------------------------------------------------------------
    // Simple record-like helpers
    // ------------------------------------------------------------------

    public record RecordingOverview(
            String filePath,
            double durationSeconds,
            Map<String, Long> eventCounts,
            Map<String, EventAvailability> availability
    ) {
    }

    /**
     * Format an {@link IQuantity} using the auto unit display.
     */
    public static String display(IQuantity quantity) {
        if (quantity == null) {
            return "N/A";
        }
        return quantity.displayUsing(IDisplayable.AUTO);
    }

    // ------------------------------------------------------------------
    // Internal cache entry with TTL
    // ------------------------------------------------------------------

    private static final class ResultCacheEntry {
        private final String result;
        private final Instant createdAt;

        ResultCacheEntry(String result) {
            this.result = result;
            this.createdAt = Instant.now();
        }

        String result() {
            return result;
        }

        boolean isExpired() {
            return Instant.now().isAfter(createdAt.plus(java.time.Duration.ofMinutes(RESULT_CACHE_TTL_MINUTES)));
        }
    }
}
