package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.VmOperationEntry;
import io.github.deplague.jmcmcp.domain.model.VmOperationsResult;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.ArrayList;
import java.util.List;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getMember;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrQuantityAggregator.*;
import static java.util.Comparator.comparing;
import static java.util.List.of;
import static org.openjdk.jmc.common.IDisplayable.AUTO;
import static org.openjdk.jmc.common.item.ItemFilters.type;
import static org.openjdk.jmc.flightrecorder.JfrAttributes.DURATION;

/**
 * Pure domain service for analyzing VM operations from JFR recordings.
 */
@Slf4j
@ApplicationScoped
public final class VmOperationsService {

    public VmOperationsResult analyze(IItemCollection events, int topN) {
        IItemCollection vmOps = events.apply(type("jdk.ExecuteVMOperation"));
        if (!vmOps.hasItems()) {
            return new VmOperationsResult(of(), "N/A", "N/A", "N/A");
        }

        IQuantity totalDuration = sumQuantity(vmOps, DURATION.getIdentifier());
        IQuantity maxDuration = maxQuantity(vmOps, DURATION.getIdentifier());
        IQuantity avgDuration = avgQuantity(vmOps, DURATION.getIdentifier());

        List<VmOperationEntry> entries = new ArrayList<>();
        for (IItemIterable iterable : vmOps) {
            for (IItem item : iterable) {
                Object operation = getMember(item, "operation").orElse(null);
                IQuantity duration = JfrAccessorRepository.<IQuantity>getQuantity(item, DURATION.getIdentifier()).orElse(null);
                Object caller = getMember(item, "caller").orElse(null);
                entries.add(new VmOperationEntry(
                        operation != null ? operation.toString() : "Unknown",
                        duration != null ? duration.displayUsing(AUTO) : "N/A",
                        caller != null ? caller.toString() : "N/A"
                ));
            }
        }

        entries.sort(comparing(VmOperationEntry::duration).reversed());
        if (entries.size() > topN) {
            entries = entries.subList(0, topN);
        }

        return new VmOperationsResult(
                entries,
                totalDuration != null ? totalDuration.displayUsing(AUTO) : "N/A",
                maxDuration != null ? maxDuration.displayUsing(AUTO) : "N/A",
                avgDuration != null ? avgDuration.displayUsing(AUTO) : "N/A"
        );
    }
}
