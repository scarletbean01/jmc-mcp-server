package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.JvmFlagEntry;
import io.github.deplague.jmcmcp.domain.model.JvmFlagsResult;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;

/**
 * Pure domain service for extracting JVM runtime flags from JFR recordings.
 */
@Slf4j
public final class JvmFlagsService {

    public JvmFlagsResult analyze(IItemCollection events, String filter) {
        List<JvmFlagEntry> flags = new ArrayList<>();
        collectFlags(events, "jdk.BooleanFlag", "boolean", flags);
        collectFlags(events, "jdk.IntFlag", "int", flags);
        collectFlags(events, "jdk.UintFlag", "uint", flags);
        collectFlags(events, "jdk.DoubleFlag", "double", flags);

        flags.sort(Comparator.comparing(JvmFlagEntry::name));

        if (filter != null && !filter.isBlank()) {
            String lower = filter.toLowerCase();
            flags = flags.stream()
                    .filter(f -> f.name().toLowerCase().contains(lower))
                    .toList();
        }

        return new JvmFlagsResult(flags);
    }

    private void collectFlags(IItemCollection events, String typeId, String typeName, List<JvmFlagEntry> flags) {
        IItemCollection filtered = events.apply(ItemFilters.type(typeId));
        for (IItemIterable iterable : filtered) {
            IMemberAccessor<Object, IItem> nameAcc = JfrItemUtils.getAccessor(iterable.getType(), "name");
            IMemberAccessor<Object, IItem> valueAcc = JfrItemUtils.getAccessor(iterable.getType(), "value");
            if (nameAcc != null && valueAcc != null) {
                for (IItem item : iterable) {
                    Object name = nameAcc.getMember(item);
                    Object value = valueAcc.getMember(item);
                    if (name != null && value != null) {
                        flags.add(new JvmFlagEntry(name.toString(), value.toString(), typeName));
                    }
                }
            }
        }
    }
}
