package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Categorical field distribution.
 */
public record EventCategoricalField(String field, List<EventFieldValue> values) {

    /**
     * A single value count.
     */
    public record EventFieldValue(String value, long count) {
    }
}
