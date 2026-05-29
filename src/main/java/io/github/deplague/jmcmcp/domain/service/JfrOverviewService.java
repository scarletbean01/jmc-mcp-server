package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.JfrOverviewResult;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrQuantityAggregator;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.flightrecorder.rules.util.RulesToolkit;

import java.util.HashMap;
import java.util.Map;

/**
 * Domain service for JFR recording overview analysis.
 */
public class JfrOverviewService {

    public JfrOverviewResult analyze(
            String filePath,
            IItemCollection fullEvents,
            IItemCollection filteredEvents,
            boolean hasTimeFilter
    ) {
        double durationSeconds = 0;
        IQuantity startTime = RulesToolkit.getEarliestStartTime(fullEvents);
        IQuantity endTime = RulesToolkit.getLatestEndTime(fullEvents);
        if (startTime != null && endTime != null) {
            durationSeconds = endTime.subtract(startTime).doubleValue();
        }

        Map<String, Long> eventCounts = new HashMap<>();
        Map<String, String> availability = new HashMap<>();
        long totalEvents = 0;

        for (IItemIterable itemIterable : fullEvents) {
            String typeId = itemIterable.getType().getIdentifier();
            long count = itemIterable.getItemCount();
            eventCounts.merge(typeId, count, Long::sum);
            totalEvents += count;

            RulesToolkit.EventAvailability avail = RulesToolkit.getEventAvailability(fullEvents, typeId);
            if (avail != null) {
                availability.put(typeId, avail.name());
            }
        }

        Long filteredEventsCount;
        filteredEventsCount = hasTimeFilter ? JfrQuantityAggregator.count(filteredEvents) : null;

        return new JfrOverviewResult(
                filePath,
                durationSeconds,
                totalEvents,
                filteredEventsCount,
                eventCounts,
                availability
        );
    }
}
