package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.DirectBufferResult;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

/**
 * Pure domain service for direct buffer statistics analysis.
 */
@Slf4j
public final class DirectBuffersService {

    public DirectBufferResult analyze(IItemCollection events) {
        IItemCollection dbEvents = events.apply(ItemFilters.type("jdk.DirectBufferStatistics"));
        if (!dbEvents.hasItems()) {
            return new DirectBufferResult(
                    Optional.empty(), Optional.empty(), Optional.empty(),
                    Optional.empty(), Optional.empty(), Optional.empty(),
                    Optional.empty(), List.of(), false
            );
        }

        IQuantity minCount = JfrItemUtils.minQuantity(dbEvents, "directBufferCount");
        IQuantity avgCount = JfrItemUtils.avgQuantity(dbEvents, "directBufferCount");
        IQuantity maxCount = JfrItemUtils.maxQuantity(dbEvents, "directBufferCount");
        IQuantity maxCapacity = JfrItemUtils.maxQuantity(dbEvents, "directTotalCapacity");
        IQuantity maxUsed = JfrItemUtils.maxQuantity(dbEvents, "directMemoryUsed");

        long maxDirectMemorySize = -1;
        IItemCollection props = events.apply(ItemFilters.type("jdk.InitialSystemProperty"));
        for (IItemIterable iterable : props) {
            var keyAcc = JfrItemUtils.getAccessor(iterable.getType(), "key");
            var valueAcc = JfrItemUtils.getAccessor(iterable.getType(), "value");
            if (keyAcc != null && valueAcc != null) {
                for (IItem item : iterable) {
                    Object key = keyAcc.getMember(item);
                    if ("sun.nio.MaxDirectMemorySize".equals(key)) {
                        Object val = valueAcc.getMember(item);
                        if (val != null) {
                            try {
                                maxDirectMemorySize = Long.parseLong(val.toString());
                            } catch (NumberFormatException ignored) {
                            }
                        }
                    }
                }
            }
        }

        Optional<Double> util = Optional.empty();
        if (maxDirectMemorySize > 0 && maxUsed != null) {
            long usedBytes = maxUsed.clampedLongValueIn(UnitLookup.BYTE);
            util = Optional.of((double) usedBytes / maxDirectMemorySize * 100.0);
        }

        List<DirectBufferResult.BufferSample> trend = new ArrayList<>();
        for (IItemIterable iterable : dbEvents) {
            var timeAcc = JfrAttributes.START_TIME.getAccessor(iterable.getType());
            var countAcc = JfrItemUtils.getAccessor(iterable.getType(), "directBufferCount");
            var capAcc = JfrItemUtils.getAccessor(iterable.getType(), "directTotalCapacity");
            var usedAcc = JfrItemUtils.getAccessor(iterable.getType(), "directMemoryUsed");

            if (timeAcc != null) {
                for (IItem item : iterable) {
                    IQuantity time = timeAcc.getMember(item);
                    if (time != null) {
                        long ms = time.clampedLongValueIn(UnitLookup.EPOCH_MS);
                        long count = countAcc != null ? ((IQuantity) countAcc.getMember(item)).longValue() : 0;
                        long cap = capAcc != null ? ((IQuantity) capAcc.getMember(item)).clampedLongValueIn(UnitLookup.BYTE) : 0;
                        long used = usedAcc != null ? ((IQuantity) usedAcc.getMember(item)).clampedLongValueIn(UnitLookup.BYTE) : 0;
                        trend.add(new DirectBufferResult.BufferSample(ms, count, cap, used));
                    }
                }
            }
        }
        trend.sort(Comparator.comparing(DirectBufferResult.BufferSample::timestampMs));

        return new DirectBufferResult(
                displayOpt(minCount),
                displayOpt(avgCount),
                displayOpt(maxCount),
                displayOpt(maxCapacity),
                displayOpt(maxUsed),
                maxDirectMemorySize > 0 ? Optional.of(formatBytes(maxDirectMemorySize)) : Optional.empty(),
                util,
                trend,
                true
        );
    }

    private static Optional<String> displayOpt(IQuantity q) {
        return q != null ? Optional.of(q.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO)) : Optional.empty();
    }

    private static String formatBytes(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024L * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0));
        }
    }
}
