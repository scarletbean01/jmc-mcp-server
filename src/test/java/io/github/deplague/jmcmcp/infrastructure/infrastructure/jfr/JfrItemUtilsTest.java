package io.github.deplague.jmcmcp.infrastructure.infrastructure.jfr;

import io.github.deplague.jmcmcp.infrastructure.jfr.JfrValueConverter;
import org.junit.jupiter.api.Test;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.common.unit.UnitLookup;

import static org.assertj.core.api.Assertions.assertThat;

class JfrItemUtilsTest {

    @Test
    void toLongHandlesIQuantity() {
        IQuantity q = UnitLookup.NUMBER_UNITY.quantity(42.0);
        assertThat(JfrValueConverter.toLong(q)).isEqualTo(42L);
    }

    @Test
    void toLongHandlesLong() {
        assertThat(JfrValueConverter.toLong(42L)).isEqualTo(42L);
    }

    @Test
    void toLongHandlesInteger() {
        assertThat(JfrValueConverter.toLong(42)).isEqualTo(42L);
    }

    @Test
    void toLongHandlesByteQuantity() {
        IQuantity q = UnitLookup.BYTE.quantity(1024.0);
        assertThat(JfrValueConverter.toLong(q)).isEqualTo(1024L);
    }

    @Test
    void toLongReturnsZeroForNull() {
        assertThat(JfrValueConverter.toLong(null)).isEqualTo(0L);
    }

    @Test
    void toLongReturnsZeroForNonNumericString() {
        assertThat(JfrValueConverter.toLong("not a number")).isEqualTo(0L);
    }

    @Test
    void toDoubleHandlesIQuantity() {
        IQuantity q = UnitLookup.NUMBER_UNITY.quantity(3.14);
        assertThat(JfrValueConverter.toDouble(q)).isEqualTo(3.14);
    }

    @Test
    void toDoubleHandlesLong() {
        assertThat(JfrValueConverter.toDouble(42L)).isEqualTo(42.0);
    }

    @Test
    void toDoubleReturnsNaNForNull() {
        assertThat(JfrValueConverter.toDouble(null)).isNaN();
    }

    @Test
    void toDoubleReturnsNaNForNonNumeric() {
        assertThat(JfrValueConverter.toDouble("hello")).isNaN();
    }
}
