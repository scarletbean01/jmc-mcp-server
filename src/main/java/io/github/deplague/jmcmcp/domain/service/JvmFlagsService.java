package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.JvmFlagEntry;
import io.github.deplague.jmcmcp.domain.model.JvmFlagsResult;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.*;

import java.util.ArrayList;
import java.util.List;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getAccessor;
import static java.util.Comparator.comparing;
import static org.openjdk.jmc.common.item.ItemFilters.type;

/**
 * Pure domain service for extracting JVM runtime flags from JFR recordings.
 */
@Slf4j
@ApplicationScoped
public final class JvmFlagsService {

    public JvmFlagsResult analyze(IItemCollection events, String filter) {
        List<JvmFlagEntry> flags = new ArrayList<>();
        collectFlags(events, "jdk.BooleanFlag", "boolean", flags);
        collectFlags(events, "jdk.IntFlag", "int", flags);
        collectFlags(events, "jdk.UintFlag", "uint", flags);
        collectFlags(events, "jdk.DoubleFlag", "double", flags);

        flags.sort(comparing(JvmFlagEntry::name));

        if (filter != null && !filter.isBlank()) {
            String lower = filter.toLowerCase();
            flags = flags.stream()
                    .filter(f -> f.name().toLowerCase().contains(lower))
                    .toList();
        }

        return new JvmFlagsResult(flags);
    }

    private void collectFlags(IItemCollection events, String typeId, String typeName, List<JvmFlagEntry> flags) {
        IItemCollection filtered = events.apply(type(typeId));
        for (IItemIterable iterable : filtered) {
            IType<?> type1 = iterable.getType();
            IMemberAccessor<Object, IItem> nameAcc = getAccessor(type1, "name");
            IType<?> type = iterable.getType();
            IMemberAccessor<Object, IItem> valueAcc = getAccessor(type, "value");
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
