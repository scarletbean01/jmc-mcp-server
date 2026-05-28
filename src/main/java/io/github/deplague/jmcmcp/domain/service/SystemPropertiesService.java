package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.SystemPropertyEntry;
import io.github.deplague.jmcmcp.domain.model.SystemPropertiesResult;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.ItemFilters;

/**
 * Pure domain service for extracting system properties from JFR recordings.
 */
@Slf4j
public final class SystemPropertiesService {

    public SystemPropertiesResult analyze(IItemCollection events, String filter) {
        IItemCollection props = events.apply(ItemFilters.type("jdk.InitialSystemProperty"));
        if (!props.hasItems()) {
            return new SystemPropertiesResult(List.of());
        }

        List<SystemPropertyEntry> entries = new ArrayList<>();
        for (IItemIterable iterable : props) {
            for (IItem item : iterable) {
                Object key = JfrItemUtils.getMember(item, "key").orElse(null);
                Object val = JfrItemUtils.getMember(item, "value").orElse(null);
                if (key != null) {
                    String keyStr = key.toString();
                    if (filter == null || keyStr.contains(filter)) {
                        entries.add(new SystemPropertyEntry(keyStr, val != null ? val.toString() : ""));
                    }
                }
            }
        }

        entries.sort(Comparator.comparing(SystemPropertyEntry::key));
        return new SystemPropertiesResult(entries);
    }
}
