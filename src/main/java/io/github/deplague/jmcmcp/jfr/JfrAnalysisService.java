package io.github.deplague.jmcmcp.jfr;

import org.openjdk.jmc.common.IDisplayable;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.flightrecorder.JfrAttributes;
import org.openjdk.jmc.flightrecorder.rules.util.RulesToolkit;
import org.openjdk.jmc.flightrecorder.rules.util.RulesToolkit.EventAvailability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

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

    /**
     * Format an {@link IQuantity} using a specific unit.
     */
    public static String displayInUnit(IQuantity quantity, String unit) {
        if (quantity == null) {
            return "N/A";
        }
        try {
            return quantity.displayUsing(IDisplayable.AUTO);
        } catch (Exception e) {
            return quantity.toString();
        }
    }

    /**
     * Check whether a given event type is available in the recording.
     */
    public static boolean hasEvents(IItemCollection events, String typeId) {
        return events.apply(ItemFilters.type(typeId)).hasItems();
    }

    /**
     * Return a short label for {@link EventAvailability}.
     */
    public static String availabilityLabel(EventAvailability avail) {
        if (avail == null) {
            return "UNKNOWN";
        }
        return switch (avail) {
            case ENABLED -> "ENABLED";
            case DISABLED -> "DISABLED";
            case UNKNOWN -> "UNKNOWN";
            case AVAILABLE -> "AVAILABLE";
            case NONE -> "NONE";
        };
    }
}
