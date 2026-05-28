package io.github.deplague.jmcmcp.domain.service;

import io.github.deplague.jmcmcp.domain.model.NativeLibraryEntry;
import io.github.deplague.jmcmcp.domain.model.NativeMemoryResult;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrItemUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.IMemberAccessor;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;

/**
 * Pure domain service for native memory tracking and library analysis.
 */
@Slf4j
public final class NativeMemoryService {

    public NativeMemoryResult analyze(IItemCollection events) {
        IItemCollection nativeLibs = events.apply(ItemFilters.type("jdk.NativeLibrary"));
        long libCount = JfrItemUtils.count(nativeLibs);

        IItemCollection heapSummary = events.apply(ItemFilters.type("jdk.GCHeapSummary"));
        IQuantity maxHeap = JfrItemUtils.maxQuantity(heapSummary, "heapSize");

        IItemCollection props = events.apply(ItemFilters.type("jdk.InitialSystemProperty"));
        Map<String, String> memProps = new HashMap<>();
        for (IItemIterable iterable : props) {
            IMemberAccessor<String, IItem> keyAccessor = JfrItemUtils.getAccessor(iterable.getType(), "key");
            IMemberAccessor<String, IItem> valueAccessor = JfrItemUtils.getAccessor(iterable.getType(), "value");
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
                IMemberAccessor<String, IItem> nameAccessor = JfrItemUtils.getAccessor(iterable.getType(), "name");
                if (nameAccessor != null) {
                    for (IItem item : iterable) {
                        if (count++ >= 50) break;
                        String name = nameAccessor.getMember(item);
                        String path = JfrItemUtils.getMember(item, "topLevelPath")
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
                maxHeap != null ? Optional.of(maxHeap.displayUsing(org.openjdk.jmc.common.IDisplayable.AUTO)) : Optional.empty(),
                memProps,
                libraries,
                libCount > 0 || maxHeap != null || !memProps.isEmpty()
        );
    }
}
