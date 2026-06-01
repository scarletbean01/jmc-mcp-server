package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.EventCategoricalField;
import io.github.deplague.jmcmcp.domain.model.EventFieldStats;
import io.github.deplague.jmcmcp.domain.model.JfrEventStatsResult;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.deplague.jmcmcp.domain.model.EventCategoricalField.EventFieldValue;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getAccessor;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrQuantityAggregator.*;
import static java.util.List.of;
import static java.util.Map.Entry;
import static org.openjdk.jmc.common.IDisplayable.AUTO;
import static org.openjdk.jmc.common.item.ItemFilters.type;

/**
 * Pure domain service for JFR event statistical summaries.
 */
@Slf4j
@ApplicationScoped
public final class JfrEventStatsService {

    public JfrEventStatsResult analyze(IItemCollection events, String eventType) {
        IItemCollection targetEvents = events.apply(type(eventType));
        long count = count(targetEvents);

        if (count == 0) {
            return new JfrEventStatsResult(eventType, 0, of(), of(), false);
        }

        if (!targetEvents.iterator().hasNext()) {
            return new JfrEventStatsResult(eventType, count, of(), of(), true);
        }

        IType<IItem> type = targetEvents.iterator().next().getType();

        List<EventFieldStats> numericFields = new ArrayList<>();
        Map<String, IAccessorKey<?>> stringFields = new HashMap<>();

        for (IAccessorKey<?> key : type.getAccessorKeys().keySet()) {
            String identifier = key.getIdentifier();
            IQuantity max = maxQuantity(targetEvents, identifier);
            if (max != null) {
                IQuantity min = minQuantity(targetEvents, identifier);
                IQuantity avg = avgQuantity(targetEvents, identifier);
                IQuantity p95 = percentileQuantity(targetEvents, identifier, 95);
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
                IType<?> type1 = iterable.getType();
                IMemberAccessor<Object, IItem> acc = getAccessor(type1, identifier);
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
                List<EventFieldValue> values = dist.entrySet().stream()
                        .sorted(Entry.<String, Long>comparingByValue().reversed())
                        .limit(5)
                        .map(e -> new EventFieldValue(e.getKey(), e.getValue()))
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
        return q != null ? q.displayUsing(AUTO) : "N/A";
    }
}
