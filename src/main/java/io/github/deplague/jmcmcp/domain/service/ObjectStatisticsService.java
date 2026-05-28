package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.ObjectStatEntry;
import io.github.deplague.jmcmcp.domain.model.ObjectStatisticsResult;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;

/**
 * Pure domain service for analyzing object statistics and heap occupancy.
 */
@Slf4j
public final class ObjectStatisticsService {

    public ObjectStatisticsResult analyze(IItemCollection events, int topN) {
        IItemCollection statsEvents = events.apply(ItemFilters.type("jdk.ObjectCount"));
        if (!statsEvents.hasItems()) {
            return new ObjectStatisticsResult(List.of(), false);
        }

        List<IItem> sortedStats = new ArrayList<>();
        for (IItemIterable iterable : statsEvents) {
            for (IItem item : iterable) {
                sortedStats.add(item);
            }
        }

        List<ObjectStatEntry> entries = sortedStats.stream()
                .sorted((a, b) -> {
                    IQuantity sa = JfrItemUtils.getQuantity(a, "totalSize").orElse(null);
                    IQuantity sb1 = JfrItemUtils.getQuantity(b, "totalSize").orElse(null);
                    if (sa == null) return (sb1 == null) ? 0 : 1;
                    if (sb1 == null) return -1;
                    return sb1.compareTo(sa);
                })
                .limit(topN)
                .map(item -> {
                    Object clazz = JfrItemUtils.getMember(item, "objectClass").orElse(null);
                    IQuantity count = JfrItemUtils.getQuantity(item, "count").orElse(null);
                    IQuantity size = JfrItemUtils.getQuantity(item, "totalSize").orElse(null);
                    return new ObjectStatEntry(
                            clazz != null ? clazz.toString() : "Unknown",
                            count != null ? count.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO) : "N/A",
                            size != null ? size.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO) : "N/A"
                    );
                })
                .toList();

        return new ObjectStatisticsResult(entries, true);
    }
}
