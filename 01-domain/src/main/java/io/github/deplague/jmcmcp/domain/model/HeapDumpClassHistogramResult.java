package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

public record HeapDumpClassHistogramResult(
        boolean hasData,
        long totalInstances,
        long totalBytes,
        List<HeapDumpClassHistogramEntry> entries
) {}
