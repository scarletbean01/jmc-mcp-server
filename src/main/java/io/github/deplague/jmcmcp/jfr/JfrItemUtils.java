package io.github.deplague.jmcmcp.jfr;

import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.IType;
import org.openjdk.jmc.common.unit.IQuantity;

/**
 * Utility methods for extracting values from JFR items without knowing the exact
 * {@link org.openjdk.jmc.common.item.IAccessorKey} types at compile time.
 */
public final class JfrItemUtils {

    private JfrItemUtils() {
        // utility class
    }

    /**
     * Get a member value from an item by attribute identifier string.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> T getMember(IItem item, String identifier) {
        final IType<?> type = item.getType();
        for (java.util.Map.Entry<org.openjdk.jmc.common.item.IAccessorKey<?>, ? extends org.openjdk.jmc.common.IDescribable> entry : type.getAccessorKeys().entrySet()) {
            if (identifier.equals(entry.getKey().getIdentifier())) {
                IMemberAccessor accessor = type.getAccessor(entry.getKey());
                return (T) accessor.getMember(item);
            }
        }
        return null;
    }

    /**
     * Get an {@link IQuantity} member from an item by attribute identifier.
     */
    public static IQuantity getQuantity(IItem item, String identifier) {
        return getMember(item, identifier);
    }

    /**
     * Sum a quantity attribute across an item collection.
     */
    public static double sumQuantity(IItemCollection items, String identifier) {
        double sum = 0;
        for (IItemIterable iterable : items) {
            for (IItem item : iterable) {
                IQuantity q = getQuantity(item, identifier);
                if (q != null) {
                    sum += q.doubleValue();
                }
            }
        }
        return sum;
    }

    /**
     * Average a quantity attribute across an item collection.
     */
    public static double avgQuantity(IItemCollection items, String identifier) {
        double sum = 0;
        long count = 0;
        for (IItemIterable iterable : items) {
            for (IItem item : iterable) {
                IQuantity q = getQuantity(item, identifier);
                if (q != null) {
                    sum += q.doubleValue();
                    count++;
                }
            }
        }
        return count == 0 ? 0 : sum / count;
    }

    /**
     * Max a quantity attribute across an item collection.
     */
    public static double maxQuantity(IItemCollection items, String identifier) {
        double max = Double.NEGATIVE_INFINITY;
        boolean any = false;
        for (IItemIterable iterable : items) {
            for (IItem item : iterable) {
                IQuantity q = getQuantity(item, identifier);
                if (q != null) {
                    double val = q.doubleValue();
                    if (!any || val > max) {
                        max = val;
                        any = true;
                    }
                }
            }
        }
        return any ? max : 0;
    }

    /**
     * Min a quantity attribute across an item collection.
     */
    public static double minQuantity(IItemCollection items, String identifier) {
        double min = Double.POSITIVE_INFINITY;
        boolean any = false;
        for (IItemIterable iterable : items) {
            for (IItem item : iterable) {
                IQuantity q = getQuantity(item, identifier);
                if (q != null) {
                    double val = q.doubleValue();
                    if (!any || val < min) {
                        min = val;
                        any = true;
                    }
                }
            }
        }
        return any ? min : 0;
    }

    /**
     * Count items in a collection.
     */
    public static long count(IItemCollection items) {
        long count = 0;
        for (IItemIterable iterable : items) {
            for (IItem ignored : iterable) {
                count++;
            }
        }
        return count;
    }
}
