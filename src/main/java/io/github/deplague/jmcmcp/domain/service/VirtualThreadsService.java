package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.VirtualThreadFailure;
import io.github.deplague.jmcmcp.domain.model.VirtualThreadPinningSite;
import io.github.deplague.jmcmcp.domain.model.VirtualThreadsResult;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getAccessor;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrQuantityAggregator.count;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrStackTraceService.formatStackTrace;
import static java.util.List.of;
import static java.util.Map.Entry;
import static org.openjdk.jmc.common.item.ItemFilters.type;

/**
 * Pure domain service for virtual thread analysis.
 */
@Slf4j
@ApplicationScoped
public final class VirtualThreadsService {

    public VirtualThreadsResult analyze(IItemCollection events, int topN) {
        IItemCollection pinned = events.apply(type("jdk.VirtualThreadPinned"));
        IItemCollection submitFailed = events.apply(type("jdk.VirtualThreadSubmitFailed"));
        IItemCollection sleepFailed = events.apply(type("jdk.VirtualThreadSleepFailed"));

        long pinnedCount = count(pinned);
        long submitFailedCount = count(submitFailed);
        long sleepFailedCount = count(sleepFailed);

        if (pinnedCount == 0 && submitFailedCount == 0 && sleepFailedCount == 0) {
            return new VirtualThreadsResult(0, 0, 0, of(), of(), of(), false);
        }

        List<VirtualThreadPinningSite> pinningSites = new ArrayList<>();
        if (pinnedCount > 0) {
            Map<String, Long> pinningMap = new HashMap<>();
            for (IItemIterable iterable : pinned) {
                IType<?> type = iterable.getType();
                IMemberAccessor<Object, IItem> stackAccessor = getAccessor(type, "stackTrace");
                if (stackAccessor != null) {
                    for (IItem item : iterable) {
                        Object stack = stackAccessor.getMember(item);
                        if (stack != null) {
                            String trace = formatStackTrace(stack, 5);
                            pinningMap.merge(trace, 1L, Long::sum);
                        }
                    }
                }
            }

            pinningSites = pinningMap.entrySet().stream()
                    .sorted(Entry.<String, Long>comparingByValue().reversed())
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
            IType<?> type = iterable.getType();
            IMemberAccessor<String, IItem> msgAccessor = getAccessor(type, "exception");
            if (msgAccessor != null) {
                for (IItem item : iterable) {
                    String msg = msgAccessor.getMember(item);
                    exceptions.merge(msg != null ? msg : "Unknown", 1L, Long::sum);
                }
            }
        }
        return exceptions.entrySet().stream()
                .sorted(Entry.<String, Long>comparingByValue().reversed())
                .map(e -> new VirtualThreadFailure(e.getKey(), e.getValue()))
                .toList();
    }
}
