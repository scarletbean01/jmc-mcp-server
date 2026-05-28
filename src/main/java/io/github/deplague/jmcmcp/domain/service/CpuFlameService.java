package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.CallPathEntry;
import io.github.deplague.jmcmcp.domain.model.CpuFlameMethodEntry;
import io.github.deplague.jmcmcp.domain.model.CpuFlameResult;
import io.github.deplague.jmcmcp.domain.model.StateDistributionEntry;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openjdk.jmc.common.IMCMethod;
import org.openjdk.jmc.common.IMCStackTrace;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;

/**
 * Pure domain service for CPU flame graph analysis.
 * Contains no MCP-specific or UI formatting logic.
 */
public final class CpuFlameService {

    public CpuFlameResult analyze(IItemCollection events, int topN) {
        IItemCollection samples = events.apply(ItemFilters.type("jdk.ExecutionSample"));
        long totalSamples = JfrItemUtils.count(samples);
        if (totalSamples == 0) {
            return new CpuFlameResult(0, List.of(), List.of(), List.of());
        }

        Map<String, Long> stateDist = new HashMap<>();
        Map<String, Long> pathDist = new HashMap<>();
        Map<String, Long> hottestMethods = new HashMap<>();
        JfrItemUtils.StackTraceFormatCache stCache = JfrItemUtils.newStackTraceFormatCache();

        for (IItemIterable iterable : samples) {
            IMemberAccessor<Object, IItem> stateAccessor =
                    JfrItemUtils.getAccessor(iterable.getType(), "state");
            IMemberAccessor<Object, IItem> stackAccessor =
                    JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");

            for (IItem item : iterable) {
                if (stateAccessor != null) {
                    Object stateObj = stateAccessor.getMember(item);
                    if (stateObj != null) {
                        stateDist.merge(stateObj.toString(), 1L, Long::sum);
                    }
                }

                if (stackAccessor != null) {
                    Object stackObj = stackAccessor.getMember(item);
                    if (stackObj instanceof IMCStackTrace trace) {
                        String path = stCache.format(stackObj, 10);
                        pathDist.merge(path, 1L, Long::sum);

                        if (trace.getFrames() != null && !trace.getFrames().isEmpty()) {
                            IMCMethod method = trace.getFrames().get(0).getMethod();
                            if (method != null) {
                                String methodName =
                                        method.getType().getFullName() +
                                                "." +
                                                method.getMethodName();
                                hottestMethods.merge(methodName, 1L, Long::sum);
                            }
                        }
                    }
                }
            }
        }

        long finalTotal = totalSamples;

        List<StateDistributionEntry> stateEntries = stateDist.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(e -> new StateDistributionEntry(
                        e.getKey(),
                        e.getValue(),
                        (e.getValue() * 100.0) / finalTotal
                ))
                .toList();

        List<CallPathEntry> pathEntries = pathDist.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(topN)
                .map(e -> new CallPathEntry(
                        e.getKey(),
                        e.getValue(),
                        (e.getValue() * 100.0) / finalTotal
                ))
                .toList();

        List<CpuFlameMethodEntry> methodEntries = hottestMethods.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(topN)
                .map(e -> new CpuFlameMethodEntry(
                        e.getKey(),
                        e.getValue(),
                        (e.getValue() * 100.0) / finalTotal
                ))
                .toList();

        return new CpuFlameResult(totalSamples, stateEntries, pathEntries, methodEntries);
    }
}
