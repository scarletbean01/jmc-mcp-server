package io.github.deplague.jmcmcp.infrastructure.jfr;

import org.openjdk.jmc.common.IDisplayable;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;

/**
 * Utility for converting JMC types to Java primitives and display strings.
 */
public final class JfrValueConverter {

    private JfrValueConverter() {
        // utility class
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

    public static IQuantity toIQuantity(Object value) {
        if (value instanceof IQuantity q) return q;
        if (value instanceof Number n) return UnitLookup.NUMBER_UNITY.quantity(n.doubleValue());
        return null;
    }

    /**
     * Format an {@link IQuantity} using the auto unit display.
     */
    public static String display(IQuantity quantity) {
        if (quantity == null) {
            return "N/A";
        }
        return quantity.displayUsing(IDisplayable.AUTO);
    }
}
