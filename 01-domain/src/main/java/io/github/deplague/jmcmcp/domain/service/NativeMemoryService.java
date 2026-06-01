package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.NativeLibraryEntry;
import io.github.deplague.jmcmcp.domain.model.NativeMemoryResult;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getAccessor;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepository.getMember;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrQuantityAggregator.count;
import static io.github.deplague.jmcmcp.infrastructure.jfr.JfrQuantityAggregator.maxQuantity;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.openjdk.jmc.common.IDisplayable.AUTO;
import static org.openjdk.jmc.common.item.ItemFilters.type;

/**
 * Pure domain service for native memory tracking and library analysis.
 */
@Slf4j
@ApplicationScoped
public final class NativeMemoryService {

    public NativeMemoryResult analyze(IItemCollection events) {
        IItemCollection nativeLibs = events.apply(type("jdk.NativeLibrary"));
        long libCount = count(nativeLibs);

        IItemCollection heapSummary = events.apply(type("jdk.GCHeapSummary"));
        IQuantity maxHeap = maxQuantity(heapSummary, "heapSize");

        IItemCollection props = events.apply(type("jdk.InitialSystemProperty"));
        Map<String, String> memProps = new HashMap<>();
        for (IItemIterable iterable : props) {
            IType<?> type1 = iterable.getType();
            IMemberAccessor<String, IItem> keyAccessor = getAccessor(type1, "key");
            IType<?> type = iterable.getType();
            IMemberAccessor<String, IItem> valueAccessor = getAccessor(type, "value");
            if (keyAccessor != null && valueAccessor != null) {
                for (IItem item : iterable) {
                    String key = keyAccessor.getMember(item);
                    if (key != null && (key.toLowerCase().contains("memory")
                            || key.toLowerCase().contains("buffer")
                            || key.toLowerCase().contains("alloc"))) {
                        memProps.put(key, valueAccessor.getMember(item));
                    }
                }
            }
        }

        List<NativeLibraryEntry> libraries = new ArrayList<>();
        if (libCount > 0) {
            int count = 0;
            for (IItemIterable iterable : nativeLibs) {
                IType<?> type = iterable.getType();
                IMemberAccessor<String, IItem> nameAccessor = getAccessor(type, "name");
                if (nameAccessor != null) {
                    for (IItem item : iterable) {
                        if (count++ >= 50) break;
                        String name = nameAccessor.getMember(item);
                        String path = getMember(item, "topLevelPath")
                                .map(Object::toString)
                                .orElse("N/A");
                        libraries.add(new NativeLibraryEntry(name, path));
                    }
                }
                if (count >= 50) break;
            }
        }

        return new NativeMemoryResult(
                libCount,
                maxHeap != null ? of(maxHeap.displayUsing(AUTO)) : empty(),
                memProps,
                libraries,
                libCount > 0 || maxHeap != null || !memProps.isEmpty()
        );
    }
}
