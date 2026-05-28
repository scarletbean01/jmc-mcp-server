package io.github.deplague.jmcmcp.domain.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Result of native memory analysis.
 */
public record NativeMemoryResult(
        long libraryCount,
        Optional<String> maxHeapSize,
        Map<String, String> memoryProperties,
        List<NativeLibraryEntry> libraries,
        boolean hasData
) {
}
