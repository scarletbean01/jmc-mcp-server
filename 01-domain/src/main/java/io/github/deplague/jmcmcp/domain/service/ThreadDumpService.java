package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.ThreadDumpEntry;
import io.github.deplague.jmcmcp.domain.model.ThreadDumpResult;
import io.github.deplague.jmcmcp.domain.port.JfrAccessorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.ArrayList;
import java.util.List;

import static java.util.List.of;
import static org.openjdk.jmc.common.item.ItemFilters.type;
import static org.openjdk.jmc.flightrecorder.JfrAttributes.START_TIME;

/**
 * Pure domain service for extracting thread dumps from JFR recordings.
 */
@Slf4j
@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = @Inject)
public final class ThreadDumpService {
    private final JfrAccessorRepository jfrAccessorRepository;

    public ThreadDumpResult analyze(IItemCollection events, int maxDumps) {
        IItemCollection threadDumps = events.apply(type("jdk.ThreadDump"));
        if (!threadDumps.hasItems()) {
            return new ThreadDumpResult(of(), false);
        }

        List<ThreadDumpEntry> entries = new ArrayList<>();
        int count = 0;
        for (IItemIterable itemIterable : threadDumps) {
            IType<?> type = itemIterable.getType();
            IMemberAccessor<Object, IItem> resultAccessor = jfrAccessorRepository.getAccessor(type, "result");
            IMemberAccessor<IQuantity, IItem> startTimeAccessor = START_TIME.getAccessor(itemIterable.getType());

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
