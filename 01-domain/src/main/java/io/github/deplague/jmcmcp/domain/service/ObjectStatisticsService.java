package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.ObjectStatEntry;
import io.github.deplague.jmcmcp.domain.model.ObjectStatisticsResult;
import io.github.deplague.jmcmcp.domain.port.JfrAccessorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.ArrayList;
import java.util.List;

import static java.util.List.of;
import static org.openjdk.jmc.common.IDisplayable.AUTO;
import static org.openjdk.jmc.common.item.ItemFilters.type;

/**
 * Pure domain service for analyzing object statistics and heap occupancy.
 */
@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = @Inject)
public final class ObjectStatisticsService {
    private final JfrAccessorRepository jfrAccessorRepository;

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
                    IQuantity sa = jfrAccessorRepository.<IQuantity>getQuantity(a, "totalSize").orElse(null);
                    IQuantity sb1 = jfrAccessorRepository.<IQuantity>getQuantity(b, "totalSize").orElse(null);
                    if (sa == null) return (sb1 == null) ? 0 : 1;
                    if (sb1 == null) return -1;
                    return sb1.compareTo(sa);
                })
                .limit(topN)
                .map(item -> {
                    Object clazz = jfrAccessorRepository.getMember(item, "objectClass").orElse(null);
                    IQuantity count = jfrAccessorRepository.<IQuantity>getQuantity(item, "count").orElse(null);
                    IQuantity size = jfrAccessorRepository.<IQuantity>getQuantity(item, "totalSize").orElse(null);
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
