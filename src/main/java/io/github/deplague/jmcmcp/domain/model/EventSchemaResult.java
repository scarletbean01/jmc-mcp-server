package io.github.deplague.jmcmcp.domain.model;

import java.util.List;
import java.util.Optional;

/**
 * Result of event schema analysis.
 */
public record EventSchemaResult(
        List<EventTypeInfo> catalog,
        Optional<EventTypeDetail> detail
) {

    public boolean isCatalog() {
        return detail.isEmpty();
    }

    /**
     * Detailed information about a specific event type.
     */
    public record EventTypeDetail(
            String typeId,
            String displayName,
            long eventCount,
            int fieldCount,
            List<EventFieldInfo> fields
    ) {
    }
}
