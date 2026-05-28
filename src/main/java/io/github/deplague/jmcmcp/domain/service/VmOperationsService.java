package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.VmOperationEntry;
import io.github.deplague.jmcmcp.domain.model.VmOperationsResult;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

/**
 * Pure domain service for analyzing VM operations from JFR recordings.
 */
@Slf4j
public final class VmOperationsService {

    public VmOperationsResult analyze(IItemCollection events, int topN) {
        IItemCollection vmOps = events.apply(ItemFilters.type("jdk.ExecuteVMOperation"));
        if (!vmOps.hasItems()) {
            return new VmOperationsResult(List.of(), "N/A", "N/A", "N/A");
        }

        IQuantity totalDuration = JfrItemUtils.sumQuantity(vmOps, JfrAttributes.DURATION.getIdentifier());
        IQuantity maxDuration = JfrItemUtils.maxQuantity(vmOps, JfrAttributes.DURATION.getIdentifier());
        IQuantity avgDuration = JfrItemUtils.avgQuantity(vmOps, JfrAttributes.DURATION.getIdentifier());

        List<VmOperationEntry> entries = new ArrayList<>();
        for (IItemIterable iterable : vmOps) {
            for (IItem item : iterable) {
                Object operation = JfrItemUtils.getMember(item, "operation").orElse(null);
                IQuantity duration = JfrItemUtils.getQuantity(item, JfrAttributes.DURATION.getIdentifier()).orElse(null);
                Object caller = JfrItemUtils.getMember(item, "caller").orElse(null);
                entries.add(new VmOperationEntry(
                        operation != null ? operation.toString() : "Unknown",
                        duration != null ? duration.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO) : "N/A",
                        caller != null ? caller.toString() : "N/A"
                ));
            }
        }

        entries.sort(Comparator.comparing(VmOperationEntry::duration).reversed());
        if (entries.size() > topN) {
            entries = entries.subList(0, topN);
        }

        return new VmOperationsResult(
                entries,
                totalDuration != null ? totalDuration.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO) : "N/A",
                maxDuration != null ? maxDuration.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO) : "N/A",
                avgDuration != null ? avgDuration.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO) : "N/A"
        );
    }
}
