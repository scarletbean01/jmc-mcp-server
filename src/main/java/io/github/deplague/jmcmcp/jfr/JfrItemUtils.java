package io.github.deplague.jmcmcp.jfr;

import org.openjdk.jmc.common.IMCFrame;
import org.openjdk.jmc.common.IMCMethod;
import org.openjdk.jmc.common.IMCStackTrace;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Utility methods for extracting values from JFR items without knowing the exact
 * {@link org.openjdk.jmc.common.item.IAccessorKey} types at compile time.
 */
public final class JfrItemUtils {

    private JfrItemUtils() {
        // utility class
    }

    /**
     * Get an accessor for a specific attribute identifier on a given type.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> IMemberAccessor<T, IItem> getAccessor(IType<?> type, String identifier) {
        for (java.util.Map.Entry<IAccessorKey<?>, ? extends org.openjdk.jmc.common.IDescribable> entry : type.getAccessorKeys().entrySet()) {
            if (identifier.equals(entry.getKey().getIdentifier())) {
                return (IMemberAccessor<T, IItem>) type.getAccessor((IAccessorKey<T>) entry.getKey());
            }
        }
        return null;
    }

    /**
     * Get a member value from an item by attribute identifier string.
     * Note: This is O(attributes) per call. Use getAccessor for batch processing.
     */
    public static <T> Optional<T> getMember(IItem item, String identifier) {
        IMemberAccessor<T, IItem> accessor = getAccessor(item.getType(), identifier);
        return accessor != null ? Optional.ofNullable(accessor.getMember(item)) : Optional.empty();
    }

    /**
     * Get an {@link IQuantity} member from an item by attribute identifier.
     */
    public static Optional<IQuantity> getQuantity(IItem item, String identifier) {
        return getMember(item, identifier);
    }

    /**
     * Sum a quantity attribute across an item collection.
     */
    public static IQuantity sumQuantity(IItemCollection items, String identifier) {
        double sum = 0;
        org.openjdk.jmc.common.unit.IUnit unit = null;
        for (IItemIterable iterable : items) {
            IMemberAccessor<IQuantity, IItem> accessor = getAccessor(iterable.getType(), identifier);
            if (accessor != null) {
                for (IItem item : iterable) {
                    IQuantity q = accessor.getMember(item);
                    if (q != null) {
                        sum += q.doubleValue();
                        if (unit == null) unit = q.getUnit();
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
        org.openjdk.jmc.common.unit.IUnit unit = null;
        for (IItemIterable iterable : items) {
            IMemberAccessor<IQuantity, IItem> accessor = getAccessor(iterable.getType(), identifier);
            if (accessor != null) {
                for (IItem item : iterable) {
                    IQuantity q = accessor.getMember(item);
                    if (q != null) {
                        sum += q.doubleValue();
                        count++;
                        if (unit == null) unit = q.getUnit();
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
            IMemberAccessor<IQuantity, IItem> accessor = getAccessor(iterable.getType(), identifier);
            if (accessor != null) {
                for (IItem item : iterable) {
                    IQuantity q = accessor.getMember(item);
                    if (q != null) {
                        if (max == null || q.compareTo(max) > 0) {
                            max = q;
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
            IMemberAccessor<IQuantity, IItem> accessor = getAccessor(iterable.getType(), identifier);
            if (accessor != null) {
                for (IItem item : iterable) {
                    IQuantity q = accessor.getMember(item);
                    if (q != null) {
                        if (min == null || q.compareTo(min) < 0) {
                            min = q;
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
            IMemberAccessor<IQuantity, IItem> accessor = getAccessor(iterable.getType(), identifier);
            if (accessor != null) {
                for (IItem item : iterable) {
                    IQuantity q = accessor.getMember(item);
                    if (q != null) {
                        values.add(q);
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
     * Count items in a collection.
     */
    public static long count(IItemCollection items) {
        long count = 0;
        for (IItemIterable iterable : items) {
            count += iterable.stream().count();
        }
        return count;
    }

    /**
     * Formats a stack trace object (expected to be an IMCStackTrace) into a truncated string.
     */
    public static String formatStackTrace(Object stackTraceObj, int maxFrames) {
        if (!(stackTraceObj instanceof IMCStackTrace stackTrace)) {
            return "No stack trace available";
        }

        List<? extends IMCFrame> frames = stackTrace.getFrames();
        if (frames == null || frames.isEmpty()) {
            return "Empty stack trace";
        }

        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (IMCFrame frame : frames) {
            if (count >= maxFrames) {
                sb.append("  ...");
                break;
            }
            IMCMethod method = frame.getMethod();
            if (method != null) {
                if (count > 0) sb.append("\n");
                sb.append("  at ");
                String typeName = method.getType().getFullName();
                sb.append(typeName).append(".").append(method.getMethodName()).append("()");
                count++;
            }
        }
        return sb.toString();
    }
}
