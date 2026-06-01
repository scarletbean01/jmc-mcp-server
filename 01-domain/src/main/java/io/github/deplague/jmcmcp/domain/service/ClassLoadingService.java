package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.ClassLoadEntry;
import io.github.deplague.jmcmcp.domain.model.ClassLoadingResult;
import io.github.deplague.jmcmcp.domain.model.ClassLoadingStats;
import io.github.deplague.jmcmcp.domain.port.JfrAccessorRepository;
import io.github.deplague.jmcmcp.domain.port.JfrQuantityAggregator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.List.of;
import static java.util.Optional.empty;
import static org.openjdk.jmc.common.IDisplayable.AUTO;
import static org.openjdk.jmc.common.item.ItemFilters.type;
import static org.openjdk.jmc.flightrecorder.JfrAttributes.DURATION;

/**
 * Pure domain service for analyzing class loading events.
 */
@Slf4j
@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = @Inject)
public final class ClassLoadingService {
    private final JfrAccessorRepository jfrAccessorRepository;
    private final JfrQuantityAggregator jfrQuantityAggregator;


    public ClassLoadingResult analyze(IItemCollection events, int topN) {
        IItemCollection classLoadEvents = events.apply(type("jdk.ClassLoad"));
        IItemCollection statsEvents = events.apply(type("jdk.ClassLoadingStatistics"));

        List<ClassLoadEntry> longestLoads = of();
        if (classLoadEvents.hasItems()) {
            List<IItem> sortedLoads = new ArrayList<>();
            for (IItemIterable iterable : classLoadEvents) {
                for (IItem item : iterable) {
                    sortedLoads.add(item);
                }
            }

            longestLoads = sortedLoads.stream()
                    .sorted((a, b) -> {
                        IQuantity da = jfrAccessorRepository.<IQuantity>getQuantity(a, DURATION.getIdentifier()).orElse(null);
                        IQuantity db = jfrAccessorRepository.<IQuantity>getQuantity(b, DURATION.getIdentifier()).orElse(null);
                        if (da == null) return (db == null) ? 0 : 1;
                        if (db == null) return -1;
                        return db.compareTo(da);
                    })
                    .limit(topN)
                    .map(item -> {
                        Object loadedClass = jfrAccessorRepository.getMember(item, "loadedClass").orElse(null);
                        IQuantity duration = jfrAccessorRepository.<IQuantity>getQuantity(item, DURATION.getIdentifier()).orElse(null);
                        Object loader = jfrAccessorRepository.getMember(item, "initiatingClassLoader").orElse(null);
                        return new ClassLoadEntry(
                                loadedClass != null ? loadedClass.toString() : "Unknown",
                                duration != null ? duration.displayUsing(AUTO) : "N/A",
                                loader != null ? loader.toString() : "N/A"
                        );
                    })
                    .toList();
        }

        ClassLoadingStats stats = new ClassLoadingStats(empty(), empty());
        if (statsEvents.hasItems()) {
            IQuantity maxLoaded = jfrQuantityAggregator.maxQuantity(statsEvents, "loadedClassCount");
            IQuantity maxUnloaded = jfrQuantityAggregator.maxQuantity(statsEvents, "unloadedClassCount");
            stats = new ClassLoadingStats(
                    maxLoaded != null ? Optional.of(maxLoaded.displayUsing(AUTO)) : empty(),
                    maxUnloaded != null ? Optional.of(maxUnloaded.displayUsing(AUTO)) : empty()
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
