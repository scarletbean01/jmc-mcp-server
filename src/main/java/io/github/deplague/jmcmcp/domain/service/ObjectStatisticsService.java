package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.ObjectStatEntry;
import io.github.deplague.jmcmcp.domain.model.ObjectStatisticsResult;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.ArrayList;
import java.util.List;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getMember;
import static java.util.List.of;
import static org.openjdk.jmc.common.IDisplayable.AUTO;
import static org.openjdk.jmc.common.item.ItemFilters.type;

/**
 * Pure domain service for analyzing object statistics and heap occupancy.
 */
@Slf4j
@ApplicationScoped
public final class ObjectStatisticsService {

    public ObjectStatisticsResult analyze(IItemCollection events, int topN) {
        IItemCollection statsEvents = events.apply(type("jdk.ObjectCount"));
        if (!statsEvents.hasItems()) {
            return new ObjectStatisticsResult(of(), false);
        }

        List<IItem> sortedStats = new ArrayList<>();
        for (IItemIterable iterable : statsEvents) {
            for (IItem item : iterable) {
                sortedStats.add(item);
            }
        }

        List<ObjectStatEntry> entries = sortedStats.stream()
                .sorted((a, b) -> {
                    IQuantity sa = JfrAccessorRepository.<IQuantity>getQuantity(a, "totalSize").orElse(null);
                    IQuantity sb1 = JfrAccessorRepository.<IQuantity>getQuantity(b, "totalSize").orElse(null);
                    if (sa == null) return (sb1 == null) ? 0 : 1;
                    if (sb1 == null) return -1;
                    return sb1.compareTo(sa);
                })
                .limit(topN)
                .map(item -> {
                    Object clazz = getMember(item, "objectClass").orElse(null);
                    IQuantity count = JfrAccessorRepository.<IQuantity>getQuantity(item, "count").orElse(null);
                    IQuantity size = JfrAccessorRepository.<IQuantity>getQuantity(item, "totalSize").orElse(null);
                    return new ObjectStatEntry(
                            clazz != null ? clazz.toString() : "Unknown",
                            count != null ? count.displayUsing(AUTO) : "N/A",
                            size != null ? size.displayUsing(AUTO) : "N/A"
                    );
                })
                .toList();

        return new ObjectStatisticsResult(entries, true);
    }
}
