package io.github.deplague.jmcmcp.domain.model;

public record HeapDumpClassHistogramEntry(
        String className,
        long instanceCount,
        long totalShallowSize,
        long totalRetainedSize
) {}
