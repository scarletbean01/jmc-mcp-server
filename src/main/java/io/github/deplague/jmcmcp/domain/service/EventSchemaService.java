package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.EventFieldInfo;
import io.github.deplague.jmcmcp.domain.model.EventSchemaResult;
import io.github.deplague.jmcmcp.domain.model.EventTypeInfo;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IAccessorKey;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IType;
import org.openjdk.jmc.common.item.ItemFilters;

/**
 * Pure domain service for discovering JFR event types and their field schemas.
 */
@Slf4j
public final class EventSchemaService {

    public EventSchemaResult analyze(IItemCollection events, String eventType) {
        if (eventType != null && !eventType.isEmpty()) {
            return analyzeDetail(events, eventType);
        }
        return analyzeCatalog(events);
    }

    private EventSchemaResult analyzeCatalog(IItemCollection events) {
        List<EventTypeInfo> typeInfos = new ArrayList<>();

        for (IItemIterable iterable : events) {
            IType<?> type = iterable.getType();
            String typeId = type.getIdentifier();
            long count = iterable.stream().count();
            int fieldCount = type.getAccessorKeys().size();
            typeInfos.add(new EventTypeInfo(typeId, type.getName(), count, fieldCount));
        }

        typeInfos.sort(Comparator.comparing(EventTypeInfo::typeId));
        return new EventSchemaResult(typeInfos, Optional.empty());
    }

    private EventSchemaResult analyzeDetail(IItemCollection events, String eventType) {
        IItemCollection filtered = events.apply(ItemFilters.type(eventType));
        if (!filtered.hasItems()) {
            return new EventSchemaResult(List.of(), Optional.of(new EventSchemaResult.EventTypeDetail(
                    eventType, null, 0, 0, List.of()
            )));
        }

        IType<?> type = null;
        long eventCount = 0;
        for (IItemIterable iterable : filtered) {
            type = iterable.getType();
            eventCount += iterable.stream().count();
        }

        if (type == null) {
            return new EventSchemaResult(List.of(), Optional.of(new EventSchemaResult.EventTypeDetail(
                    eventType, null, 0, 0, List.of()
            )));
        }

        List<EventFieldInfo> fields = new ArrayList<>();
        List<java.util.Map.Entry<IAccessorKey<?>, ? extends org.openjdk.jmc.common.IDescribable>> entries =
                new ArrayList<>(type.getAccessorKeys().entrySet());
        entries.sort(Comparator.comparing(e -> e.getKey().getIdentifier()));

        for (var entry : entries) {
            String fieldId = entry.getKey().getIdentifier();
            String description = entry.getValue().getDescription();
            fields.add(new EventFieldInfo(fieldId, description != null ? description : ""));
        }

        EventSchemaResult.EventTypeDetail detail = new EventSchemaResult.EventTypeDetail(
                eventType,
                type.getName(),
                eventCount,
                type.getAccessorKeys().size(),
                fields
        );
        return new EventSchemaResult(List.of(), Optional.of(detail));
    }
}
