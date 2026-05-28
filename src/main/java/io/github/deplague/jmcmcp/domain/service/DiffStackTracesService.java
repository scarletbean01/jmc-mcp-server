package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.DiffStackTracesResult;
import io.github.deplague.jmcmcp.domain.model.MethodDiffEntry;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.rules.util.RulesToolkit;

/**
 * Pure domain service for method-level diff between two JFR recordings.
 * Contains no MCP-specific or UI formatting logic.
 */
public final class DiffStackTracesService {

    public DiffStackTracesResult analyze(
            IItemCollection baselineEvents,
            IItemCollection targetEvents,
            String packagePrefix,
            int topN) {

        double baselineDurationSec = getDurationSeconds(baselineEvents);
        double targetDurationSec = getDurationSeconds(targetEvents);
        if (baselineDurationSec <= 0) {
            baselineDurationSec = 1.0;
        }
        if (targetDurationSec <= 0) {
            targetDurationSec = 1.0;
        }

        Map<String, Long> baselineMethods = extractMethodCounts(baselineEvents, packagePrefix);
        Map<String, Long> targetMethods = extractMethodCounts(targetEvents, packagePrefix);

        Map<String, Double> baselineRates = normalizeRates(baselineMethods, baselineDurationSec);
        Map<String, Double> targetRates = normalizeRates(targetMethods, targetDurationSec);

        Set<String> allMethods = new LinkedHashSet<>();
        allMethods.addAll(baselineRates.keySet());
        allMethods.addAll(targetRates.keySet());

        List<MethodDiffEntry> newMethods = new ArrayList<>();
        List<MethodDiffEntry> disappearedMethods = new ArrayList<>();
        List<MethodDiffEntry> changedMethods = new ArrayList<>();
        List<MethodDiffEntry> stableMethods = new ArrayList<>();

        for (String method : allMethods) {
            double bRate = baselineRates.getOrDefault(method, 0.0);
            double tRate = targetRates.getOrDefault(method, 0.0);
            double absChange = tRate - bRate;
            double pctChange = bRate > 0
                    ? ((tRate - bRate) / bRate) * 100.0
                    : (tRate > 0 ? Double.POSITIVE_INFINITY : 0.0);

            MethodDiffEntry diff = new MethodDiffEntry(method, bRate, tRate, absChange, pctChange);

            if (bRate == 0 && tRate > 0) {
                newMethods.add(diff);
            } else if (tRate == 0 && bRate > 0) {
                disappearedMethods.add(diff);
            } else if (Math.abs(pctChange) > 20.0) {
                changedMethods.add(diff);
            } else {
                stableMethods.add(diff);
            }
        }

        newMethods.sort(Comparator.comparingDouble(MethodDiffEntry::targetRate).reversed());
        disappearedMethods.sort(Comparator.comparingDouble(MethodDiffEntry::baselineRate).reversed());
        changedMethods.sort((a, b) -> Double.compare(Math.abs(b.pctChange()), Math.abs(a.pctChange())));

        long baselineTotal = baselineMethods.values().stream().mapToLong(Long::longValue).sum();
        long targetTotal = targetMethods.values().stream().mapToLong(Long::longValue).sum();

        return new DiffStackTracesResult(
                baselineDurationSec,
                targetDurationSec,
                baselineTotal,
                targetTotal,
                newMethods,
                disappearedMethods,
                changedMethods,
                stableMethods
        );
    }

    private double getDurationSeconds(IItemCollection events) {
        IQuantity start = RulesToolkit.getEarliestStartTime(events);
        IQuantity end = RulesToolkit.getLatestEndTime(events);
        if (start != null && end != null) {
            return end.subtract(start).doubleValueIn(UnitLookup.SECOND);
        }
        return 1.0;
    }

    private Map<String, Long> extractMethodCounts(IItemCollection events, String packagePrefix) {
        Map<String, Long> methodCounts = new HashMap<>();
        IItemCollection samples = events.apply(ItemFilters.type("jdk.ExecutionSample"));

        for (IItemIterable iterable : samples) {
            IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(
                    iterable.getType(),
                    "stackTrace"
            );
            if (stackAccessor == null) {
                continue;
            }

            for (IItem item : iterable) {
                Object st = stackAccessor.getMember(item);
                if (st == null) {
                    continue;
                }

                String trace = JfrItemUtils.formatStackTraceFocusingOn(st, 1, packagePrefix);
                if (trace == null || trace.isEmpty()
                        || trace.startsWith("No frames") || trace.startsWith("Empty")) {
                    continue;
                }

                methodCounts.merge(trace, 1L, Long::sum);
            }
        }

        return methodCounts;
    }

    private Map<String, Double> normalizeRates(Map<String, Long> methodCounts, double durationSec) {
        Map<String, Double> rates = new HashMap<>();
        for (var entry : methodCounts.entrySet()) {
            rates.put(entry.getKey(), entry.getValue() / durationSec);
        }
        return rates;
    }
}
