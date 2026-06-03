package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

public record DominatorTreeResult(
        boolean hasData,
        long totalObjects,
        long totalClasses,
        long totalHeapBytes,
        List<HeapObjectEntry> topDominators
) {}
