package io.github.deplague.jmcmcp.jfr;

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

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Core service for loading JFR recordings and extracting summary / availability metadata.
 */
public final class JfrAnalysisService {

    private static final Logger LOG = LoggerFactory.getLogger(JfrAnalysisService.class);
    private static final int MAX_CACHE_ENTRIES = 50;

    private final JfrRecordingCache cache;
    private final Map<String, String> resultCache = Collections.synchronizedMap(new LinkedHashMap<>(MAX_CACHE_ENTRIES, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
            return size() > MAX_CACHE_ENTRIES;
        }
    });

    public JfrAnalysisService(JfrRecordingCache cache) {
        this.cache = cache;
    }

    /**
     * Get a cached analysis result if available.
     */
    public String getCachedResult(String filePath, String toolName, Map<String, Object> args) {
        String key = filePath + ":" + toolName + ":" + args.toString();
        String result = resultCache.get(key);
        if (result != null) {
            LOG.debug("Analysis cache hit for {}: {}", toolName, filePath);
        }
        return result;
    }

    /**
     * Cache an analysis result.
     */
    public void cacheResult(String filePath, String toolName, Map<String, Object> args, String result) {
        String key = filePath + ":" + toolName + ":" + args.toString();
        resultCache.put(key, result);
    }

    /**
     * Load (or retrieve from cache) the events for the given recording path.
     */
    public IItemCollection loadRecording(String filePath) throws IOException {
        return cache.load(filePath);
    }

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
}
