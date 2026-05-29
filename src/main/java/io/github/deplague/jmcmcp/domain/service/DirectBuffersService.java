package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.DirectBufferResult;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IType;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.github.deplague.jmcmcp.domain.model.DirectBufferResult.BufferSample;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getAccessor;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrQuantityAggregator.batchStats;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrQuantityAggregator.maxQuantity;
import static java.lang.Long.parseLong;
import static java.lang.String.format;
import static java.util.Comparator.comparing;
import static java.util.List.of;
import static java.util.Optional.empty;
import static org.openjdk.jmc.common.IDisplayable.AUTO;
import static org.openjdk.jmc.common.item.ItemFilters.type;
import static org.openjdk.jmc.common.unit.UnitLookup.BYTE;
import static org.openjdk.jmc.common.unit.UnitLookup.EPOCH_MS;
import static org.openjdk.jmc.flightrecorder.JfrAttributes.START_TIME;

/**
 * Pure domain service for direct buffer statistics analysis.
 */
@Slf4j
@ApplicationScoped
public final class DirectBuffersService {

    public DirectBufferResult analyze(IItemCollection events) {
        IItemCollection dbEvents = events.apply(type("jdk.DirectBufferStatistics"));
        if (!dbEvents.hasItems()) {
            return new DirectBufferResult(
                    empty(), empty(), empty(),
                    empty(), empty(), empty(),
                    empty(), of(), false
            );
        }

        var countStats = batchStats(dbEvents, "directBufferCount");
        IQuantity minCount = countStats.get("min");
        IQuantity avgCount = countStats.get("avg");
        IQuantity maxCount = countStats.get("max");
        var memStats = batchStats(dbEvents, "directMemoryUsed");
        IQuantity maxUsed = memStats.get("max");
        IQuantity maxCapacity = maxQuantity(dbEvents, "directTotalCapacity");

        long maxDirectMemorySize = -1;
        IItemCollection props = events.apply(type("jdk.InitialSystemProperty"));
        for (IItemIterable iterable : props) {
            IType<?> type1 = iterable.getType();
            var keyAcc = getAccessor(type1, "key");
            IType<?> type = iterable.getType();
            var valueAcc = getAccessor(type, "value");
            if (keyAcc != null && valueAcc != null) {
                for (IItem item : iterable) {
                    Object key = keyAcc.getMember(item);
                    if ("sun.nio.MaxDirectMemorySize".equals(key)) {
                        Object val = valueAcc.getMember(item);
                        if (val != null) {
                            try {
                                maxDirectMemorySize = parseLong(val.toString());
                            } catch (NumberFormatException ignored) {
                            }
                        }
                    }
                }
            }
        }

        Optional<Double> util = empty();
        if (maxDirectMemorySize > 0 && maxUsed != null) {
            long usedBytes = maxUsed.clampedLongValueIn(BYTE);
            util = Optional.of((double) usedBytes / maxDirectMemorySize * 100.0);
        }

        List<BufferSample> trend = new ArrayList<>();
        for (IItemIterable iterable : dbEvents) {
            var timeAcc = START_TIME.getAccessor(iterable.getType());
            IType<?> type2 = iterable.getType();
            var countAcc = getAccessor(type2, "directBufferCount");
            IType<?> type1 = iterable.getType();
            var capAcc = getAccessor(type1, "directTotalCapacity");
            IType<?> type = iterable.getType();
            var usedAcc = getAccessor(type, "directMemoryUsed");

            if (timeAcc != null) {
                for (IItem item : iterable) {
                    IQuantity time = timeAcc.getMember(item);
                    if (time != null) {
                        long ms = time.clampedLongValueIn(EPOCH_MS);
                        long count = countAcc != null ? ((IQuantity) countAcc.getMember(item)).longValue() : 0;
                        long cap = capAcc != null ? ((IQuantity) capAcc.getMember(item)).clampedLongValueIn(BYTE) : 0;
                        long used = usedAcc != null ? ((IQuantity) usedAcc.getMember(item)).clampedLongValueIn(BYTE) : 0;
                        trend.add(new BufferSample(ms, count, cap, used));
                    }
                }
            }
        }
        trend.sort(comparing(BufferSample::timestampMs));

        return new DirectBufferResult(
                displayOpt(minCount),
                displayOpt(avgCount),
                displayOpt(maxCount),
                displayOpt(maxCapacity),
                displayOpt(maxUsed),
                maxDirectMemorySize > 0 ? Optional.of(formatBytes(maxDirectMemorySize)) : empty(),
                util,
                trend,
                true
        );
    }

    private static Optional<String> displayOpt(IQuantity q) {
        return q != null ? Optional.of(q.displayUsing(AUTO)) : empty();
    }

    private static String formatBytes(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024L * 1024 * 1024) {
            return format("%.2f MB", bytes / (1024.0 * 1024.0));
        } else {
            return format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0));
        }
    }
}
