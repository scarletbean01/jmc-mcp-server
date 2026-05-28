package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.SearchEventEntry;
import io.github.deplague.jmcmcp.domain.model.SearchEventsResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IAccessorKey;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.ItemFilters;

/**
 * Pure domain service for searching specific JFR events.
 */
@Slf4j
public final class SearchEventsService {

    public SearchEventsResult search(IItemCollection events, String eventType, int limit) {
        String displayName = "Unknown";
        for (IItemIterable iter : events) {
            if (iter.getType().getIdentifier().equals(eventType)) {
                displayName = iter.getType().getName();
                break;
            }
        }

        IItemCollection filtered = events.apply(ItemFilters.type(eventType));
        if (!filtered.hasItems()) {
            return new SearchEventsResult(eventType, displayName, List.of(), false);
        }

        List<SearchEventEntry> entries = new ArrayList<>();
        int count = 0;
        for (IItemIterable itemIterable : filtered) {
            var type = itemIterable.getType();
            var keyMap = type.getAccessorKeys();

            for (IItem item : itemIterable) {
                if (count >= limit) break;

                Map<String, String> fields = new HashMap<>();
                for (var entry : keyMap.entrySet()) {
                    IAccessorKey<?> key = entry.getKey();
                    Object val = type.getAccessor(key).getMember(item);
                    if (val != null) {
                        String valStr = val.toString();
                        if (valStr.length() > 500) {
                            valStr = valStr.substring(0, 500) + "... [truncated]";
                        }
                        fields.put(key.getIdentifier(), valStr);
                    }
                }
                entries.add(new SearchEventEntry(count + 1, fields));
                count++;
            }
            if (count >= limit) break;
        }

        return new SearchEventsResult(eventType, displayName, entries, true);
    }
}
