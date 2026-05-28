package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.GcAnalysisResult;
import io.github.deplague.jmcmcp.domain.model.GcFrequencies;
import io.github.deplague.jmcmcp.domain.model.GcHeapSummary;
import io.github.deplague.jmcmcp.domain.model.GcPauseTimes;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.Optional;
import org.openjdk.jmc.common.IDisplayable;
import org.openjdk.jmc.common.item.Aggregators;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

/**
 * Pure domain service for GC analysis.
 */
public final class GcAnalysisService {

    public GcAnalysisResult analyze(IItemCollection events, String detailLevel) {
        var gcPauses = events.apply(ItemFilters.type("jdk.GCPhasePause"));
        var youngGC = events.apply(ItemFilters.type("jdk.YoungGarbageCollection"));
        var oldGC = events.apply(ItemFilters.type("jdk.OldGarbageCollection"));
        var heapSummary = events.apply(ItemFilters.type("jdk.GCHeapSummary"));

        boolean hasData = gcPauses.hasItems() || youngGC.hasItems() || oldGC.hasItems() || heapSummary.hasItems();

        Optional<GcPauseTimes> pauseTimes = Optional.empty();
        if (gcPauses.hasItems() && ("all".equals(detailLevel) || "pause_times".equals(detailLevel))) {
            IQuantity avgPause = JfrItemUtils.avgQuantity(gcPauses, JfrAttributes.DURATION.getIdentifier());
            IQuantity maxPause = JfrItemUtils.maxQuantity(gcPauses, JfrAttributes.DURATION.getIdentifier());
            IQuantity totalPause = JfrItemUtils.sumQuantity(gcPauses, JfrAttributes.DURATION.getIdentifier());
            pauseTimes = Optional.of(new GcPauseTimes(display(avgPause), display(maxPause), display(totalPause)));
        }

        Optional<GcFrequencies> frequencies = Optional.empty();
        if ((youngGC.hasItems() || oldGC.hasItems()) && ("all".equals(detailLevel) || "frequencies".equals(detailLevel))) {
            long youngCount = youngGC.hasItems() ? youngGC.getAggregate(Aggregators.count()).longValue() : 0;
            long oldCount = oldGC.hasItems() ? oldGC.getAggregate(Aggregators.count()).longValue() : 0;
            frequencies = Optional.of(new GcFrequencies(youngCount, oldCount));
        }

        Optional<GcHeapSummary> heap = Optional.empty();
        if (("all".equals(detailLevel) || "heap_summary".equals(detailLevel)) && heapSummary.hasItems()) {
            IQuantity maxHeap = JfrItemUtils.maxQuantity(heapSummary, "heapUsed");
            IQuantity minHeap = JfrItemUtils.minQuantity(heapSummary, "heapUsed");
            IQuantity avgHeap = JfrItemUtils.avgQuantity(heapSummary, "heapUsed");
            heap = Optional.of(new GcHeapSummary(display(maxHeap), display(minHeap), display(avgHeap)));
        }

        return new GcAnalysisResult(pauseTimes, frequencies, heap, hasData);
    }

    private static String display(IQuantity q) {
        if (q == null) {
            return "N/A";
        }
        return q.displayUsing(IDisplayable.AUTO);
    }
}
