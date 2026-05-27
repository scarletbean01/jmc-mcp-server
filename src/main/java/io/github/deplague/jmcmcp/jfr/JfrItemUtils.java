package io.github.deplague.jmcmcp.jfr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import org.openjdk.jmc.common.IMCFrame;
import org.openjdk.jmc.common.IMCMethod;
import org.openjdk.jmc.common.IMCStackTrace;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;

/**
 * Utility methods for extracting values from JFR items without knowing the exact
 * {@link org.openjdk.jmc.common.item.IAccessorKey} types at compile time.
 */
public final class JfrItemUtils {

    // Cache accessors per type to avoid O(attributes) linear scan on every call.
    // JFR types are immutable, so this is safe without invalidation.
    @SuppressWarnings("rawtypes")
    private static final Map<
        IType<?>,
        Map<String, IMemberAccessor>
    > ACCESSOR_CACHE = new ConcurrentHashMap<>();

    private JfrItemUtils() {
        // utility class
    }

    /**
     * Get an accessor for a specific attribute identifier on a given type.
     * Results are cached per type to eliminate repeated O(attributes) scans.
     */
    @SuppressWarnings({ "unchecked" })
    public static <T> IMemberAccessor<T, IItem> getAccessor(
        IType<?> type,
        String identifier
    ) {
        Map<String, IMemberAccessor> typeCache = ACCESSOR_CACHE.computeIfAbsent(
            type,
            t -> new ConcurrentHashMap<>()
        );
        return (IMemberAccessor<T, IItem>) typeCache.computeIfAbsent(
            identifier,
            id -> {
                for (java.util.Map.Entry<
                    IAccessorKey<?>,
                    ? extends org.openjdk.jmc.common.IDescribable
                > entry : type.getAccessorKeys().entrySet()) {
                    if (id.equals(entry.getKey().getIdentifier())) {
                        return type.getAccessor(
                            (IAccessorKey<Object>) entry.getKey()
                        );
                    }
                }
                return null;
            }
        );
    }

    /**
     * Get a member value from an item by attribute identifier string.
     * Note: This is O(attributes) per call. Use getAccessor for batch processing.
     */
    public static <T> Optional<T> getMember(IItem item, String identifier) {
        IMemberAccessor<T, IItem> accessor = getAccessor(
            item.getType(),
            identifier
        );
        return accessor != null
            ? Optional.ofNullable(accessor.getMember(item))
            : Optional.empty();
    }

    /**
     * Get an {@link IQuantity} member from an item by attribute identifier.
     */
    public static Optional<IQuantity> getQuantity(
        IItem item,
        String identifier
    ) {
        return getMember(item, identifier);
    }

    public static double toDouble(Object value) {
        if (value instanceof IQuantity q) return q.doubleValue();
        if (value instanceof Number n) return n.doubleValue();
        return Double.NaN;
    }

    public static long toLong(Object value) {
        if (value instanceof IQuantity q) return q.longValue();
        if (value instanceof Number n) return n.longValue();
        return 0L;
    }

    private static IQuantity toIQuantity(Object value) {
        if (value instanceof IQuantity q) return q;
        if (value instanceof Number n) return UnitLookup.NUMBER_UNITY.quantity(
            n.doubleValue()
        );
        return null;
    }

    /**
     * Sum a quantity attribute across an item collection.
     * Handles both IQuantity and Number-typed attributes.
     */
    public static IQuantity sumQuantity(
        IItemCollection items,
        String identifier
    ) {
        double sum = 0;
        org.openjdk.jmc.common.unit.IUnit unit = null;
        for (IItemIterable iterable : items) {
            IMemberAccessor<Object, IItem> accessor = getAccessor(
                iterable.getType(),
                identifier
            );
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
    public static IQuantity avgQuantity(
        IItemCollection items,
        String identifier
    ) {
        double sum = 0;
        long count = 0;
        org.openjdk.jmc.common.unit.IUnit unit = null;
        for (IItemIterable iterable : items) {
            IMemberAccessor<Object, IItem> accessor = getAccessor(
                iterable.getType(),
                identifier
            );
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
    public static IQuantity maxQuantity(
        IItemCollection items,
        String identifier
    ) {
        IQuantity max = null;
        for (IItemIterable iterable : items) {
            IMemberAccessor<Object, IItem> accessor = getAccessor(
                iterable.getType(),
                identifier
            );
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
    public static IQuantity minQuantity(
        IItemCollection items,
        String identifier
    ) {
        IQuantity min = null;
        for (IItemIterable iterable : items) {
            IMemberAccessor<Object, IItem> accessor = getAccessor(
                iterable.getType(),
                identifier
            );
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
    public static IQuantity percentileQuantity(
        IItemCollection items,
        String identifier,
        double percentile
    ) {
        List<IQuantity> values = new ArrayList<>();
        for (IItemIterable iterable : items) {
            IMemberAccessor<Object, IItem> accessor = getAccessor(
                iterable.getType(),
                identifier
            );
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
        int index = (int) Math.max(
            0,
            Math.ceil((percentile / 100.0) * values.size()) - 1
        );
        return values.get(index);
    }

    /**
     * Batch-compute min, max, avg, and percentiles in a single pass.
     * Returns a map with keys "min", "max", "avg", and "p{percentile}".
     * This eliminates redundant iteration when multiple statistics are needed.
     */
    public static Map<String, IQuantity> batchStats(
        IItemCollection items,
        String identifier,
        double... percentiles
    ) {
        double sum = 0;
        long count = 0;
        IQuantity min = null;
        IQuantity max = null;
        org.openjdk.jmc.common.unit.IUnit unit = null;
        List<IQuantity> values = null;

        boolean needPercentile = percentiles != null && percentiles.length > 0;

        for (IItemIterable iterable : items) {
            IMemberAccessor<Object, IItem> accessor = getAccessor(
                iterable.getType(),
                identifier
            );
            if (accessor != null) {
                for (IItem item : iterable) {
                    Object raw = accessor.getMember(item);
                    if (raw != null) {
                        IQuantity q = toIQuantity(raw);
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

        Map<String, IQuantity> result = new java.util.HashMap<>();
        if (count == 0 || unit == null) {
            return result;
        }

        result.put("min", min);
        result.put("max", max);
        result.put("avg", unit.quantity(sum / count));

        if (values != null && !values.isEmpty()) {
            Collections.sort(values);
            for (double p : percentiles) {
                int index = (int) Math.max(
                    0,
                    Math.ceil((p / 100.0) * values.size()) - 1
                );
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

    /**
     * Format a stack trace object with all frames (no truncation).
     * Used by stack_trace_search and request_waterfall tools.
     */
    public static String formatFullStackTrace(Object stackTraceObj) {
        return formatStackTrace(stackTraceObj, Integer.MAX_VALUE);
    }

    /**
     * Check whether any frame in a stack trace matches the given regex pattern.
     * Avoids the cost of formatting the full trace string for non-matching events.
     */
    public static boolean stackTraceMatches(
        Object stackTraceObj,
        Pattern pattern
    ) {
        if (!(stackTraceObj instanceof IMCStackTrace stackTrace)) {
            return false;
        }
        List<? extends IMCFrame> frames = stackTrace.getFrames();
        if (frames == null || frames.isEmpty()) {
            return false;
        }
        for (IMCFrame frame : frames) {
            IMCMethod method = frame.getMethod();
            if (method == null) continue;
            String typeName = method.getType().getFullName();
            String methodName = method.getMethodName();
            if (
                pattern.matcher(typeName).find() ||
                pattern.matcher(methodName).find() ||
                pattern.matcher(typeName + "." + methodName).find()
            ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Formats a stack trace object into a truncated string.
     */
    public static String formatStackTrace(Object stackTraceObj, int maxFrames) {
        return formatStackTraceFocusingOn(stackTraceObj, maxFrames, null);
    }

    /**
     * Creates a reusable stack-trace formatting cache backed by {@link IdentityHashMap}.
     * JFR deduplicates stack trace instances, so caching by identity dramatically
     * reduces redundant string allocation when formatting the same trace millions of times.
     */
    public static StackTraceFormatCache newStackTraceFormatCache() {
        return new StackTraceFormatCache();
    }

    /**
     * Thread-safe reusable cache for formatted stack traces.
     */
    public static final class StackTraceFormatCache {

        private final IdentityHashMap<Object, String> cache =
            new IdentityHashMap<>();

        public String format(Object stackTraceObj, int maxFrames) {
            return cache.computeIfAbsent(stackTraceObj, k ->
                formatStackTrace(k, maxFrames)
            );
        }

        public String formatFocusingOn(
            Object stackTraceObj,
            int maxFrames,
            String packagePrefix
        ) {
            return cache.computeIfAbsent(stackTraceObj, k ->
                formatStackTraceFocusingOn(k, maxFrames, packagePrefix)
            );
        }

        public int size() {
            return cache.size();
        }
    }

    /**
     * Formats a stack trace object, optionally focusing on a specific package prefix by skipping framework internals.
     */
    public static String formatStackTraceFocusingOn(
        Object stackTraceObj,
        int maxFrames,
        String packagePrefix
    ) {
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

            if (
                packagePrefix != null &&
                !packagePrefix.isBlank() &&
                !foundPrefix
            ) {
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
            sb.append("  at ")
                .append(typeName)
                .append(".")
                .append(method.getMethodName())
                .append("():")
                .append(frame.getFrameLineNumber());
            count++;
        }

        if (count == 0 && packagePrefix != null && !packagePrefix.isBlank()) {
            return "No frames matched package prefix: " + packagePrefix;
        }

        return sb.toString();
    }
}
