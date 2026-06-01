package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.CompilationEntry;
import io.github.deplague.jmcmcp.domain.model.CompilerFailureEntry;
import io.github.deplague.jmcmcp.domain.model.DeoptimizationEntry;
import io.github.deplague.jmcmcp.domain.model.JitCompilationResult;
import io.github.deplague.jmcmcp.domain.port.JfrAccessorRepository;
import io.github.deplague.jmcmcp.domain.port.JfrQuantityAggregator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.IType;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.*;

import static java.lang.String.valueOf;
import static java.util.List.of;
import static java.util.Map.Entry;
import static java.util.Optional.empty;
import static org.openjdk.jmc.common.IDisplayable.AUTO;
import static org.openjdk.jmc.common.item.Aggregators.count;
import static org.openjdk.jmc.common.item.ItemFilters.type;
import static org.openjdk.jmc.flightrecorder.JfrAttributes.DURATION;

/**
 * Pure domain service for JIT compilation and deoptimization analysis.
 */
@Slf4j
@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = @Inject)
public final class JitCompilationService {
    private final JfrAccessorRepository jfrAccessorRepository;
    private final JfrQuantityAggregator jfrQuantityAggregator;

    public JitCompilationResult analyze(IItemCollection events, int topN) {
        var compilations = events.apply(type("jdk.Compilation"));
        var deopts = events.apply(type("jdk.Deoptimization"));
        var failures = events.apply(type("jdk.CompilerFailure"));

        boolean hasCompilations = compilations.hasItems();
        boolean hasDeopts = deopts.hasItems();
        boolean hasFailures = failures.hasItems();

        Optional<String> totalCount = empty();
        Optional<String> avgDuration = empty();
        Optional<String> maxDuration = empty();
        List<CompilationEntry> longestComp = of();

        if (hasCompilations) {
            totalCount = displayOpt(compilations.getAggregate(count()));
            var compStats = jfrQuantityAggregator.batchStats(compilations, DURATION.getIdentifier());
            avgDuration = displayOpt(compStats.get("avg"));
            maxDuration = displayOpt(compStats.get("max"));

            List<IItem> sortedComp = new ArrayList<>();
            compilations.forEach(iterable -> iterable.forEach(sortedComp::add));
            longestComp = sortedComp.stream()
                    .sorted((a, b) -> {
                        IQuantity da = (IQuantity) jfrAccessorRepository.getQuantity(a, DURATION.getIdentifier()).orElse(null);
                        IQuantity db = (IQuantity) jfrAccessorRepository.getQuantity(b, DURATION.getIdentifier()).orElse(null);
                        if (da == null) return (db == null) ? 0 : 1;
                        if (db == null) return -1;
                        return db.compareTo(da);
                    })
                    .limit(topN)
                    .map(item -> {
                        Object method = jfrAccessorRepository.getMember(item, "method").orElse(null);
                        IQuantity duration = (IQuantity) jfrAccessorRepository.getQuantity(item, DURATION.getIdentifier()).orElse(null);
                        Object level = jfrAccessorRepository.getMember(item, "compilationId").orElse(null);
                        return new CompilationEntry(
                                method != null ? method.toString() : "Unknown",
                                duration != null ? duration.displayUsing(AUTO) : "N/A",
                                level != null ? level.toString() : "N/A"
                        );
                    })
                    .toList();
        }

        Optional<String> totalDeopts = empty();
        List<DeoptimizationEntry> topDeopts = of();
        if (hasDeopts) {
            totalDeopts = displayOpt(deopts.getAggregate(count()));

            Map<String, Integer> methodDeopts = new HashMap<>();
            for (var itemIterable : deopts) {
                IType<?> type = itemIterable.getType();
                IMemberAccessor<Object, IItem> methodAccessor = jfrAccessorRepository.getAccessor(type, "method");
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
                    .sorted(Entry.<String, Integer>comparingByValue().reversed())
                    .limit(topN)
                    .map(e -> new DeoptimizationEntry(e.getKey(), e.getValue()))
                    .toList();
        }

        List<CompilerFailureEntry> failureEntries = of();
        if (hasFailures) {
            failureEntries = new ArrayList<>();
            for (var itemIterable : failures) {
                IType<?> type1 = itemIterable.getType();
                IMemberAccessor<Object, IItem> methodAccessor = jfrAccessorRepository.getAccessor(type1, "method");
                IType<?> type = itemIterable.getType();
                IMemberAccessor<Object, IItem> msgAccessor = jfrAccessorRepository.getAccessor(type, "failureMessage");
                if (methodAccessor != null && msgAccessor != null) {
                    for (IItem item : itemIterable) {
                        failureEntries.add(new CompilerFailureEntry(
                                valueOf(methodAccessor.getMember(item)),
                                valueOf(msgAccessor.getMember(item))
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
        return q != null ? Optional.of(q.displayUsing(AUTO)) : empty();
    }
}