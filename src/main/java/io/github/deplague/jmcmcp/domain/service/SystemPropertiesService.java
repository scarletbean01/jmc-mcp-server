package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.SystemPropertiesResult;
import io.github.deplague.jmcmcp.domain.model.SystemPropertyEntry;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;

import java.util.ArrayList;
import java.util.List;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getMember;
import static java.util.Comparator.comparing;
import static java.util.List.of;
import static org.openjdk.jmc.common.item.ItemFilters.type;

/**
 * Pure domain service for extracting system properties from JFR recordings.
 */
@Slf4j
@ApplicationScoped
public final class SystemPropertiesService {

    public SystemPropertiesResult analyze(IItemCollection events, String filter) {
        IItemCollection props = events.apply(type("jdk.InitialSystemProperty"));
        if (!props.hasItems()) {
            return new SystemPropertiesResult(of());
        }

        List<SystemPropertyEntry> entries = new ArrayList<>();
        for (IItemIterable iterable : props) {
            for (IItem item : iterable) {
                Object key = getMember(item, "key").orElse(null);
                Object val = getMember(item, "value").orElse(null);
                if (key != null) {
                    String keyStr = key.toString();
                    if (filter == null || keyStr.contains(filter)) {
                        entries.add(new SystemPropertyEntry(keyStr, val != null ? val.toString() : ""));
                    }
                }
            }
        }

        entries.sort(comparing(SystemPropertyEntry::key));
        return new SystemPropertiesResult(entries);
    }
}
