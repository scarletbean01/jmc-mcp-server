package io.github.deplague.jmcmcp.jfr;

import org.openjdk.jmc.common.IMCFrame;
import org.openjdk.jmc.common.IMCMethod;
import org.openjdk.jmc.common.IMCStackTrace;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;
import org.openjdk.jmc.flightrecorder.IParserStats;

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
    @SuppressWarnings({"unchecked"})
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

    public static double toDouble(Object value) {
        if (value instanceof IQuantity q) return q.doubleValue();
        if (value instanceof Number n) return n.doubleValue();
        return Double.NaN;
    }

    private static IQuantity toIQuantity(Object value) {
        if (value instanceof IQuantity q) return q;
        if (value instanceof Number n) return UnitLookup.NUMBER_UNITY.quantity(n.doubleValue());
        return null;
    }

    /**
     * Sum a quantity attribute across an item collection.
     * Handles both IQuantity and Number-typed attributes.
     */
    public static IQuantity sumQuantity(IItemCollection items, String identifier) {
        double sum = 0;
        org.openjdk.jmc.common.unit.IUnit unit = null;
        for (IItemIterable iterable : items) {
            IMemberAccessor<Object, IItem> accessor = getAccessor(iterable.getType(), identifier);
            if (accessor != null) {
                for (IItem item : iterable) {
                    Object raw = accessor.getMember(item);
                    if (raw != null) {
                        IQuantity q = toIQuantity(raw);
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
     * Handles both IQuantity and Number-typed attributes.
     */
    public static IQuantity avgQuantity(IItemCollection items, String identifier) {
        double sum = 0;
        long count = 0;
        org.openjdk.jmc.common.unit.IUnit unit = null;
        for (IItemIterable iterable : items) {
            IMemberAccessor<Object, IItem> accessor = getAccessor(iterable.getType(), identifier);
            if (accessor != null) {
                for (IItem item : iterable) {
                    Object raw = accessor.getMember(item);
                    if (raw != null) {
                        IQuantity q = toIQuantity(raw);
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
     * Handles both IQuantity and Number-typed attributes.
     */
    public static IQuantity maxQuantity(IItemCollection items, String identifier) {
        IQuantity max = null;
        for (IItemIterable iterable : items) {
            IMemberAccessor<Object, IItem> accessor = getAccessor(iterable.getType(), identifier);
            if (accessor != null) {
                for (IItem item : iterable) {
                    Object raw = accessor.getMember(item);
                    if (raw != null) {
                        IQuantity q = toIQuantity(raw);
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
     * Handles both IQuantity and Number-typed attributes.
     */
    public static IQuantity minQuantity(IItemCollection items, String identifier) {
        IQuantity min = null;
        for (IItemIterable iterable : items) {
            IMemberAccessor<Object, IItem> accessor = getAccessor(iterable.getType(), identifier);
            if (accessor != null) {
                for (IItem item : iterable) {
                    Object raw = accessor.getMember(item);
                    if (raw != null) {
                        IQuantity q = toIQuantity(raw);
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
     * Handles both IQuantity and Number-typed attributes.
     */
    public static IQuantity percentileQuantity(IItemCollection items, String identifier, double percentile) {
        List<IQuantity> values = new ArrayList<>();
        for (IItemIterable iterable : items) {
            IMemberAccessor<Object, IItem> accessor = getAccessor(iterable.getType(), identifier);
            if (accessor != null) {
                for (IItem item : iterable) {
                    Object raw = accessor.getMember(item);
                    if (raw != null) {
                        IQuantity q = toIQuantity(raw);
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
     * Count items in a collection.
     */
    public static long count(IItemCollection items) {
        long count = 0;
        for (IItemIterable iterable : items) {
            count += iterable.getItemCount();
        }
        return count;
    }

    /**
     * Formats a stack trace object into a truncated string.
     */
    public static String formatStackTrace(Object stackTraceObj, int maxFrames) {
        return formatStackTraceFocusingOn(stackTraceObj, maxFrames, null);
    }

    /**
     * Formats a stack trace object, optionally focusing on a specific package prefix by skipping framework internals.
     */
    public static String formatStackTraceFocusingOn(Object stackTraceObj, int maxFrames, String packagePrefix) {
        if (!(stackTraceObj instanceof IMCStackTrace stackTrace)) {
            return "No stack trace available";
        }

        List<? extends IMCFrame> frames = stackTrace.getFrames();
        if (frames == null || frames.isEmpty()) {
            return "Empty stack trace";
        }

        StringBuilder sb = new StringBuilder();
        int count = 0;
        boolean foundPrefix = false;

        for (IMCFrame frame : frames) {
            IMCMethod method = frame.getMethod();
            if (method == null) continue;
            
            String typeName = method.getType().getFullName();
            
            if (packagePrefix != null && !packagePrefix.isBlank() && !foundPrefix) {
                if (typeName.startsWith(packagePrefix)) {
                    foundPrefix = true;
                } else {
                    continue; // Skip framework frames until we hit the business logic
                }
            }

            if (count >= maxFrames) {
                sb.append("  ...");
                break;
            }
            
            if (count > 0) sb.append("\n");
            sb.append("  at ").append(typeName).append(".").append(method.getMethodName()).append("()");
            count++;
        }
        
        if (count == 0 && packagePrefix != null && !packagePrefix.isBlank()) {
            return "No frames matched package prefix: " + packagePrefix;
        }
        
        return sb.toString();
    }
}
