package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.CompilationEntry;
import io.github.deplague.jmcmcp.domain.model.CompilerFailureEntry;
import io.github.deplague.jmcmcp.domain.model.DeoptimizationEntry;
import io.github.deplague.jmcmcp.domain.model.JitCompilationResult;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.Aggregators;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

/**
 * Pure domain service for JIT compilation and deoptimization analysis.
 */
@Slf4j
public final class JitCompilationService {

    public JitCompilationResult analyze(IItemCollection events, int topN) {
        var compilations = events.apply(ItemFilters.type("jdk.Compilation"));
        var deopts = events.apply(ItemFilters.type("jdk.Deoptimization"));
        var failures = events.apply(ItemFilters.type("jdk.CompilerFailure"));

        boolean hasCompilations = compilations.hasItems();
        boolean hasDeopts = deopts.hasItems();
        boolean hasFailures = failures.hasItems();

        Optional<String> totalCount = Optional.empty();
        Optional<String> avgDuration = Optional.empty();
        Optional<String> maxDuration = Optional.empty();
        List<CompilationEntry> longestComp = List.of();

        if (hasCompilations) {
            totalCount = displayOpt(compilations.getAggregate(Aggregators.count()));
            avgDuration = displayOpt(compilations.getAggregate(Aggregators.avg(JfrAttributes.DURATION)));
            maxDuration = displayOpt(JfrItemUtils.maxQuantity(compilations, JfrAttributes.DURATION.getIdentifier()));

            List<IItem> sortedComp = new ArrayList<>();
            compilations.forEach(iterable -> iterable.forEach(sortedComp::add));
            longestComp = sortedComp.stream()
                    .sorted((a, b) -> {
                        IQuantity da = JfrItemUtils.getQuantity(a, JfrAttributes.DURATION.getIdentifier()).orElse(null);
                        IQuantity db = JfrItemUtils.getQuantity(b, JfrAttributes.DURATION.getIdentifier()).orElse(null);
                        if (da == null) return (db == null) ? 0 : 1;
                        if (db == null) return -1;
                        return db.compareTo(da);
                    })
                    .limit(topN)
                    .map(item -> {
                        Object method = JfrItemUtils.getMember(item, "method").orElse(null);
                        IQuantity duration = JfrItemUtils.getQuantity(item, JfrAttributes.DURATION.getIdentifier()).orElse(null);
                        Object level = JfrItemUtils.getMember(item, "compilationId").orElse(null);
                        return new CompilationEntry(
                                method != null ? method.toString() : "Unknown",
                                duration != null ? duration.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO) : "N/A",
                                level != null ? level.toString() : "N/A"
                        );
                    })
                    .toList();
        }

        Optional<String> totalDeopts = Optional.empty();
        List<DeoptimizationEntry> topDeopts = List.of();
        if (hasDeopts) {
            totalDeopts = displayOpt(deopts.getAggregate(Aggregators.count()));

            Map<String, Integer> methodDeopts = new HashMap<>();
            for (var itemIterable : deopts) {
                IMemberAccessor<Object, IItem> methodAccessor = JfrItemUtils.getAccessor(itemIterable.getType(), "method");
                if (methodAccessor != null) {
                    for (IItem item : itemIterable) {
                        Object method = methodAccessor.getMember(item);
                        if (method != null) {
                            methodDeopts.merge(method.toString(), 1, Integer::sum);
                        }
                    }
                }
            }

            topDeopts = methodDeopts.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(topN)
                    .map(e -> new DeoptimizationEntry(e.getKey(), e.getValue()))
                    .toList();
        }

        List<CompilerFailureEntry> failureEntries = List.of();
        if (hasFailures) {
            failureEntries = new ArrayList<>();
            for (var itemIterable : failures) {
                IMemberAccessor<Object, IItem> methodAccessor = JfrItemUtils.getAccessor(itemIterable.getType(), "method");
                IMemberAccessor<Object, IItem> msgAccessor = JfrItemUtils.getAccessor(itemIterable.getType(), "failureMessage");
                if (methodAccessor != null && msgAccessor != null) {
                    for (IItem item : itemIterable) {
                        failureEntries.add(new CompilerFailureEntry(
                                String.valueOf(methodAccessor.getMember(item)),
                                String.valueOf(msgAccessor.getMember(item))
                        ));
                    }
                }
            }
        }

        return new JitCompilationResult(
                totalCount,
                avgDuration,
                maxDuration,
                longestComp,
                totalDeopts,
                topDeopts,
                failureEntries,
                hasCompilations || hasDeopts || hasFailures
        );
    }

    private static Optional<String> displayOpt(IQuantity q) {
        return q != null ? Optional.of(q.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO)) : Optional.empty();
    }
}
