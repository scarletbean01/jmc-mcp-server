package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

public record UnifiedClassEntry(
        String className,
        long jfrSampleCount,
        List<String> jfrAllocationSites,
        long heapInstanceCount,
        long heapRetainedSize,
        String severity
) {
}
