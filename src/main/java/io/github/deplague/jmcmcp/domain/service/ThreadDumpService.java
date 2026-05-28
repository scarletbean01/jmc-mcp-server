package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.ThreadDumpEntry;
import io.github.deplague.jmcmcp.domain.model.ThreadDumpResult;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

/**
 * Pure domain service for extracting thread dumps from JFR recordings.
 */
@Slf4j
public final class ThreadDumpService {

    public ThreadDumpResult analyze(IItemCollection events, int maxDumps) {
        IItemCollection threadDumps = events.apply(ItemFilters.type("jdk.ThreadDump"));
        if (!threadDumps.hasItems()) {
            return new ThreadDumpResult(List.of(), false);
        }

        List<ThreadDumpEntry> entries = new ArrayList<>();
        int count = 0;
        for (IItemIterable itemIterable : threadDumps) {
            IMemberAccessor<Object, IItem> resultAccessor = JfrItemUtils.getAccessor(itemIterable.getType(), "result");
            IMemberAccessor<IQuantity, IItem> startTimeAccessor = JfrAttributes.START_TIME.getAccessor(itemIterable.getType());

            if (resultAccessor != null && startTimeAccessor != null) {
                for (IItem item : itemIterable) {
                    if (count >= maxDumps) break;

                    Object result = resultAccessor.getMember(item);
                    Object startTime = startTimeAccessor.getMember(item);

                    if (result != null) {
                        entries.add(new ThreadDumpEntry(
                                startTime != null ? startTime.toString() : "Unknown",
                                result.toString()
                        ));
                        count++;
                    }
                }
            }
            if (count >= maxDumps) break;
        }

        return new ThreadDumpResult(entries, !entries.isEmpty());
    }
}
