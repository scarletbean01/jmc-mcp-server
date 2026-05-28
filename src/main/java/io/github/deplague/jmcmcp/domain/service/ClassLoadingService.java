package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.ClassLoadEntry;
import io.github.deplague.jmcmcp.domain.model.ClassLoadingResult;
import io.github.deplague.jmcmcp.domain.model.ClassLoadingStats;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

/**
 * Pure domain service for analyzing class loading events.
 */
@Slf4j
public final class ClassLoadingService {

    public ClassLoadingResult analyze(IItemCollection events, int topN) {
        IItemCollection classLoadEvents = events.apply(ItemFilters.type("jdk.ClassLoad"));
        IItemCollection statsEvents = events.apply(ItemFilters.type("jdk.ClassLoadingStatistics"));

        List<ClassLoadEntry> longestLoads = List.of();
        if (classLoadEvents.hasItems()) {
            List<IItem> sortedLoads = new ArrayList<>();
            for (IItemIterable iterable : classLoadEvents) {
                for (IItem item : iterable) {
                    sortedLoads.add(item);
                }
            }

            longestLoads = sortedLoads.stream()
                    .sorted((a, b) -> {
                        IQuantity da = JfrItemUtils.getQuantity(a, JfrAttributes.DURATION.getIdentifier()).orElse(null);
                        IQuantity db = JfrItemUtils.getQuantity(b, JfrAttributes.DURATION.getIdentifier()).orElse(null);
                        if (da == null) return (db == null) ? 0 : 1;
                        if (db == null) return -1;
                        return db.compareTo(da);
                    })
                    .limit(topN)
                    .map(item -> {
                        Object loadedClass = JfrItemUtils.getMember(item, "loadedClass").orElse(null);
                        IQuantity duration = JfrItemUtils.getQuantity(item, JfrAttributes.DURATION.getIdentifier()).orElse(null);
                        Object loader = JfrItemUtils.getMember(item, "initiatingClassLoader").orElse(null);
                        return new ClassLoadEntry(
                                loadedClass != null ? loadedClass.toString() : "Unknown",
                                duration != null ? duration.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO) : "N/A",
                                loader != null ? loader.toString() : "N/A"
                        );
                    })
                    .toList();
        }

        ClassLoadingStats stats = new ClassLoadingStats(Optional.empty(), Optional.empty());
        if (statsEvents.hasItems()) {
            IQuantity maxLoaded = JfrItemUtils.maxQuantity(statsEvents, "loadedClassCount");
            IQuantity maxUnloaded = JfrItemUtils.maxQuantity(statsEvents, "unloadedClassCount");
            stats = new ClassLoadingStats(
                    maxLoaded != null ? Optional.of(maxLoaded.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO)) : Optional.empty(),
                    maxUnloaded != null ? Optional.of(maxUnloaded.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO)) : Optional.empty()
            );
        }

        return new ClassLoadingResult(
                longestLoads,
                stats,
                classLoadEvents.hasItems(),
                statsEvents.hasItems()
        );
    }
}
