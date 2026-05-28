package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.EventCategoricalField;
import io.github.deplague.jmcmcp.domain.model.EventFieldStats;
import io.github.deplague.jmcmcp.domain.model.JfrEventStatsResult;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IAccessorKey;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.IType;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;

/**
 * Pure domain service for JFR event statistical summaries.
 */
@Slf4j
public final class JfrEventStatsService {

    public JfrEventStatsResult analyze(IItemCollection events, String eventType) {
        IItemCollection targetEvents = events.apply(ItemFilters.type(eventType));
        long count = JfrItemUtils.count(targetEvents);

        if (count == 0) {
            return new JfrEventStatsResult(eventType, 0, List.of(), List.of(), false);
        }

        if (!targetEvents.iterator().hasNext()) {
            return new JfrEventStatsResult(eventType, count, List.of(), List.of(), true);
        }

        IType<IItem> type = targetEvents.iterator().next().getType();

        List<EventFieldStats> numericFields = new ArrayList<>();
        Map<String, IAccessorKey<?>> stringFields = new HashMap<>();

        for (IAccessorKey<?> key : type.getAccessorKeys().keySet()) {
            String identifier = key.getIdentifier();
            IQuantity max = JfrItemUtils.maxQuantity(targetEvents, identifier);
            if (max != null) {
                IQuantity min = JfrItemUtils.minQuantity(targetEvents, identifier);
                IQuantity avg = JfrItemUtils.avgQuantity(targetEvents, identifier);
                IQuantity p95 = JfrItemUtils.percentileQuantity(targetEvents, identifier, 95);
                numericFields.add(new EventFieldStats(
                        identifier,
                        display(min),
                        display(avg),
                        display(max),
                        display(p95)
                ));
            } else {
                stringFields.put(identifier, key);
            }
        }

        List<EventCategoricalField> categoricalFields = new ArrayList<>();
        for (String identifier : stringFields.keySet()) {
            if ("startTime".equals(identifier) || "endTime".equals(identifier)
                    || "duration".equals(identifier) || "stackTrace".equals(identifier)) {
                continue;
            }
            Map<String, Long> dist = new HashMap<>();
            for (IItemIterable iterable : targetEvents) {
                IMemberAccessor<Object, IItem> acc = JfrItemUtils.getAccessor(iterable.getType(), identifier);
                if (acc != null) {
                    for (IItem item : iterable) {
                        Object val = acc.getMember(item);
                        if (val != null) {
                            dist.merge(val.toString(), 1L, Long::sum);
                        }
                    }
                }
            }
            if (!dist.isEmpty() && dist.size() < count) {
                List<EventCategoricalField.EventFieldValue> values = dist.entrySet().stream()
                        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                        .limit(5)
                        .map(e -> new EventCategoricalField.EventFieldValue(e.getKey(), e.getValue()))
                        .toList();
                categoricalFields.add(new EventCategoricalField(identifier, values));
            }
        }

        return new JfrEventStatsResult(
                eventType,
                count,
                numericFields,
                categoricalFields,
                true
        );
    }

    private static String display(IQuantity q) {
        return q != null ? q.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO) : "N/A";
    }
}
