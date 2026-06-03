package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

public record HeapObjectEntry(
        long objectId,
        String className,
        long shallowSize,
        long retainedSize,
        int instanceCount,
        List<HeapObjectEntry> children,
        boolean hasChildren
) {}
