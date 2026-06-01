package io.github.deplague.jmcmcp.domain.port;

import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.Map;

public interface JfrQuantityAggregator {
    IQuantity sumQuantity(IItemCollection items, String identifier);

    IQuantity avgQuantity(IItemCollection items, String identifier);

    IQuantity maxQuantity(IItemCollection items, String identifier);

    IQuantity minQuantity(IItemCollection items, String identifier);

    IQuantity percentileQuantity(IItemCollection items, String identifier, double percentile);

    Map<String, IQuantity> batchStats(IItemCollection items, String identifier, double... percentiles);

    long count(IItemCollection items);
}
