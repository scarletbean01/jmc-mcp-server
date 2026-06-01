package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.GcAnalysisResult;
import io.github.deplague.jmcmcp.domain.model.GcFrequencies;
import io.github.deplague.jmcmcp.domain.model.GcHeapSummary;
import io.github.deplague.jmcmcp.domain.model.GcPauseTimes;
import jakarta.enterprise.context.ApplicationScoped;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.Optional;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrQuantityAggregator.batchStats;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrQuantityAggregator.sumQuantity;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.openjdk.jmc.common.IDisplayable.AUTO;
import static org.openjdk.jmc.common.item.Aggregators.count;
import static org.openjdk.jmc.common.item.ItemFilters.type;
import static org.openjdk.jmc.flightrecorder.JfrAttributes.DURATION;

/**
 * Pure domain service for GC analysis.
 */
@ApplicationScoped
public final class GcAnalysisService {

    public GcAnalysisResult analyze(IItemCollection events, String detailLevel) {
        var gcPauses = events.apply(type("jdk.GCPhasePause"));
        var youngGC = events.apply(type("jdk.YoungGarbageCollection"));
        var oldGC = events.apply(type("jdk.OldGarbageCollection"));
        var heapSummary = events.apply(type("jdk.GCHeapSummary"));

        boolean hasData = gcPauses.hasItems() || youngGC.hasItems() || oldGC.hasItems() || heapSummary.hasItems();

        Optional<GcPauseTimes> pauseTimes = empty();
        if (gcPauses.hasItems() && ("all".equals(detailLevel) || "pause_times".equals(detailLevel))) {
            var stats = batchStats(gcPauses, DURATION.getIdentifier());
            IQuantity totalPause = sumQuantity(gcPauses, DURATION.getIdentifier());
            pauseTimes = of(new GcPauseTimes(display(stats.get("avg")), display(stats.get("max")), display(totalPause)));
        }

        Optional<GcFrequencies> frequencies = empty();
        if ((youngGC.hasItems() || oldGC.hasItems()) && ("all".equals(detailLevel) || "frequencies".equals(detailLevel))) {
            long youngCount = youngGC.hasItems() ? youngGC.getAggregate(count()).longValue() : 0;
            long oldCount = oldGC.hasItems() ? oldGC.getAggregate(count()).longValue() : 0;
            frequencies = of(new GcFrequencies(youngCount, oldCount));
        }

        Optional<GcHeapSummary> heap = empty();
        if (("all".equals(detailLevel) || "heap_summary".equals(detailLevel)) && heapSummary.hasItems()) {
            var stats = batchStats(heapSummary, "heapUsed");
            heap = of(new GcHeapSummary(display(stats.get("max")), display(stats.get("min")), display(stats.get("avg"))));
        }

        return new GcAnalysisResult(pauseTimes, frequencies, heap, hasData);
    }

    private static String display(IQuantity q) {
        if (q == null) {
            return "N/A";
        }
        return q.displayUsing(AUTO);
    }
}
