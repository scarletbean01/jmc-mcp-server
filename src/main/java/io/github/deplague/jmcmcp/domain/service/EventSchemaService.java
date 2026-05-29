package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.EventFieldInfo;
import io.github.deplague.jmcmcp.domain.model.EventSchemaResult;
import io.github.deplague.jmcmcp.domain.model.EventTypeInfo;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.IDescribable;
import org.openjdk.jmc.common.item.IAccessorKey;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.github.deplague.jmcmcp.domain.model.EventSchemaResult.EventTypeDetail;
import static java.util.Comparator.comparing;
import static java.util.List.of;
import static java.util.Optional.empty;
import static org.openjdk.jmc.common.item.ItemFilters.type;

/**
 * Pure domain service for discovering JFR event types and their field schemas.
 */
@Slf4j
@ApplicationScoped
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
            long count = iterable.getItemCount();
            int fieldCount = type.getAccessorKeys().size();
            typeInfos.add(new EventTypeInfo(typeId, type.getName(), count, fieldCount));
        }

        typeInfos.sort(comparing(EventTypeInfo::typeId));
        return new EventSchemaResult(typeInfos, empty());
    }

    private EventSchemaResult analyzeDetail(IItemCollection events, String eventType) {
        IItemCollection filtered = events.apply(type(eventType));
        if (!filtered.hasItems()) {
            return new EventSchemaResult(of(), Optional.of(new EventTypeDetail(
                    eventType, null, 0, 0, of()
            )));
        }

        IType<?> type = null;
        long eventCount = 0;
        for (IItemIterable iterable : filtered) {
            type = iterable.getType();
            eventCount += iterable.getItemCount();
        }

        if (type == null) {
            return new EventSchemaResult(of(), Optional.of(new EventTypeDetail(
                    eventType, null, 0, 0, of()
            )));
        }

        List<EventFieldInfo> fields = new ArrayList<>();
        List<Map.Entry<IAccessorKey<?>, ? extends IDescribable>> entries =
                new ArrayList<>(type.getAccessorKeys().entrySet());
        entries.sort(comparing(e -> e.getKey().getIdentifier()));

        for (var entry : entries) {
            String fieldId = entry.getKey().getIdentifier();
            String description = entry.getValue().getDescription();
            fields.add(new EventFieldInfo(fieldId, description != null ? description : ""));
        }

        EventTypeDetail detail = new EventTypeDetail(
                eventType,
                type.getName(),
                eventCount,
                type.getAccessorKeys().size(),
                fields
        );
        return new EventSchemaResult(of(), Optional.of(detail));
    }
}
