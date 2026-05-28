package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.VirtualThreadFailure;
import io.github.deplague.jmcmcp.domain.model.VirtualThreadPinningSite;
import io.github.deplague.jmcmcp.domain.model.VirtualThreadsResult;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;

/**
 * Pure domain service for virtual thread analysis.
 */
@Slf4j
public final class VirtualThreadsService {

    public VirtualThreadsResult analyze(IItemCollection events, int topN) {
        IItemCollection pinned = events.apply(ItemFilters.type("jdk.VirtualThreadPinned"));
        IItemCollection submitFailed = events.apply(ItemFilters.type("jdk.VirtualThreadSubmitFailed"));
        IItemCollection sleepFailed = events.apply(ItemFilters.type("jdk.VirtualThreadSleepFailed"));

        long pinnedCount = JfrItemUtils.count(pinned);
        long submitFailedCount = JfrItemUtils.count(submitFailed);
        long sleepFailedCount = JfrItemUtils.count(sleepFailed);

        if (pinnedCount == 0 && submitFailedCount == 0 && sleepFailedCount == 0) {
            return new VirtualThreadsResult(0, 0, 0, List.of(), List.of(), List.of(), false);
        }

        List<VirtualThreadPinningSite> pinningSites = new ArrayList<>();
        if (pinnedCount > 0) {
            Map<String, Long> pinningMap = new HashMap<>();
            for (IItemIterable iterable : pinned) {
                IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");
                if (stackAccessor != null) {
                    for (IItem item : iterable) {
                        Object stack = stackAccessor.getMember(item);
                        if (stack != null) {
                            String trace = JfrItemUtils.formatStackTrace(stack, 5);
                            pinningMap.merge(trace, 1L, Long::sum);
                        }
                    }
                }
            }

            pinningSites = pinningMap.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(topN)
                    .map(e -> new VirtualThreadPinningSite(
                            e.getKey(),
                            e.getValue(),
                            (e.getValue() * 100.0) / pinnedCount
                    ))
                    .toList();
        }

        List<VirtualThreadFailure> submitFailures = extractFailures(submitFailed);
        List<VirtualThreadFailure> sleepFailures = extractFailures(sleepFailed);

        return new VirtualThreadsResult(
                pinnedCount,
                submitFailedCount,
                sleepFailedCount,
                pinningSites,
                submitFailures,
                sleepFailures,
                true
        );
    }

    private List<VirtualThreadFailure> extractFailures(IItemCollection events) {
        Map<String, Long> exceptions = new HashMap<>();
        for (IItemIterable iterable : events) {
            IMemberAccessor<String, IItem> msgAccessor = JfrItemUtils.getAccessor(iterable.getType(), "exception");
            if (msgAccessor != null) {
                for (IItem item : iterable) {
                    String msg = msgAccessor.getMember(item);
                    exceptions.merge(msg != null ? msg : "Unknown", 1L, Long::sum);
                }
            }
        }
        return exceptions.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(e -> new VirtualThreadFailure(e.getKey(), e.getValue()))
                .toList();
    }
}
