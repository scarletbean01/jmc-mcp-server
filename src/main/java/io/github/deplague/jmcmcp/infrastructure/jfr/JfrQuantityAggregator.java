package io.github.deplague.jmcmcp.infrastructure.jfr;

import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.IUnit;

import java.util.*;

/**
 * Aggregator for computing statistics across JFR item collections.
 */
public final class JfrQuantityAggregator {

    private JfrQuantityAggregator() {
        // utility class
    }

    /**
     * Sum a quantity attribute across an item collection.
     */
    public static IQuantity sumQuantity(IItemCollection items, String identifier) {
        double sum = 0;
        IUnit unit = null;
        for (IItemIterable iterable : items) {
            IMemberAccessor<Object, IItem> accessor = JfrAccessorRepository.getAccessor(iterable.getType(), identifier);
            if (accessor != null) {
                for (IItem item : iterable) {
                    Object raw = accessor.getMember(item);
                    if (raw != null) {
                        IQuantity q = JfrValueConverter.toIQuantity(raw);
                        if (q != null) {
                            sum += q.doubleValue();
                            if (unit == null) unit = q.getUnit();
                        }
                    }
                }
            }
        }
        return unit != null ? unit.quantity(sum) : null;
    }

    /**
     * Average a quantity attribute across an item collection.
     */
    public static IQuantity avgQuantity(IItemCollection items, String identifier) {
        double sum = 0;
        long count = 0;
        IUnit unit = null;
        for (IItemIterable iterable : items) {
            IMemberAccessor<Object, IItem> accessor = JfrAccessorRepository.getAccessor(iterable.getType(), identifier);
            if (accessor != null) {
                for (IItem item : iterable) {
                    Object raw = accessor.getMember(item);
                    if (raw != null) {
                        IQuantity q = JfrValueConverter.toIQuantity(raw);
                        if (q != null) {
                            sum += q.doubleValue();
                            count++;
                            if (unit == null) unit = q.getUnit();
                        }
                    }
                }
            }
        }
        return (count == 0 || unit == null) ? null : unit.quantity(sum / count);
    }

    /**
     * Max a quantity attribute across an item collection.
     */
    public static IQuantity maxQuantity(IItemCollection items, String identifier) {
        IQuantity max = null;
        for (IItemIterable iterable : items) {
            IMemberAccessor<Object, IItem> accessor = JfrAccessorRepository.getAccessor(iterable.getType(), identifier);
            if (accessor != null) {
                for (IItem item : iterable) {
                    Object raw = accessor.getMember(item);
                    if (raw != null) {
                        IQuantity q = JfrValueConverter.toIQuantity(raw);
                        if (q != null) {
                            if (max == null || q.compareTo(max) > 0) {
                                max = q;
                            }
                        }
                    }
                }
            }
        }
        return max;
    }

    /**
     * Min a quantity attribute across an item collection.
     */
    public static IQuantity minQuantity(IItemCollection items, String identifier) {
        IQuantity min = null;
        for (IItemIterable iterable : items) {
            IMemberAccessor<Object, IItem> accessor = JfrAccessorRepository.getAccessor(iterable.getType(), identifier);
            if (accessor != null) {
                for (IItem item : iterable) {
                    Object raw = accessor.getMember(item);
                    if (raw != null) {
                        IQuantity q = JfrValueConverter.toIQuantity(raw);
                        if (q != null) {
                            if (min == null || q.compareTo(min) < 0) {
                                min = q;
                            }
                        }
                    }
                }
            }
        }
        return min;
    }

    /**
     * Calculate a percentile for a quantity attribute across an item collection.
     */
    public static IQuantity percentileQuantity(IItemCollection items, String identifier, double percentile) {
        List<IQuantity> values = new ArrayList<>();
        for (IItemIterable iterable : items) {
            IMemberAccessor<Object, IItem> accessor = JfrAccessorRepository.getAccessor(iterable.getType(), identifier);
            if (accessor != null) {
                for (IItem item : iterable) {
                    Object raw = accessor.getMember(item);
                    if (raw != null) {
                        IQuantity q = JfrValueConverter.toIQuantity(raw);
                        if (q != null) {
                            values.add(q);
                        }
                    }
                }
            }
        }
        if (values.isEmpty()) {
            return null;
        }
        Collections.sort(values);
        int index = (int) Math.max(0, Math.ceil((percentile / 100.0) * values.size()) - 1);
        return values.get(index);
    }

    /**
     * Batch-compute statistics in a single pass.
     */
    public static Map<String, IQuantity> batchStats(IItemCollection items, String identifier, double... percentiles) {
        double sum = 0;
        long count = 0;
        IQuantity min = null;
        IQuantity max = null;
        IUnit unit = null;
        List<IQuantity> values = null;

        boolean needPercentile = percentiles != null && percentiles.length > 0;

        for (IItemIterable iterable : items) {
            IMemberAccessor<Object, IItem> accessor = JfrAccessorRepository.getAccessor(iterable.getType(), identifier);
            if (accessor != null) {
                for (IItem item : iterable) {
                    Object raw = accessor.getMember(item);
                    if (raw != null) {
                        IQuantity q = JfrValueConverter.toIQuantity(raw);
                        if (q != null) {
                            sum += q.doubleValue();
                            count++;
                            if (min == null || q.compareTo(min) < 0) min = q;
                            if (max == null || q.compareTo(max) > 0) max = q;
                            if (unit == null) unit = q.getUnit();
                            if (needPercentile) {
                                if (values == null) values = new ArrayList<>();
                                values.add(q);
                            }
                        }
                    }
                }
            }
        }

        Map<String, IQuantity> result = new HashMap<>();
        if (count == 0 || unit == null) {
            return result;
        }

        result.put("min", min);
        result.put("max", max);
        result.put("avg", unit.quantity(sum / count));

        if (values != null && !values.isEmpty()) {
            Collections.sort(values);
            for (double p : percentiles) {
                int index = (int) Math.max(0, Math.ceil((p / 100.0) * values.size()) - 1);
                result.put("p" + (int) p, values.get(index));
            }
        }
        return result;
    }

    /**
     * Count items in a collection.
     */
    public static long count(IItemCollection items) {
        long count = 0;
        for (IItemIterable iterable : items) {
            count += iterable.getItemCount();
        }
        return count;
    }
}
