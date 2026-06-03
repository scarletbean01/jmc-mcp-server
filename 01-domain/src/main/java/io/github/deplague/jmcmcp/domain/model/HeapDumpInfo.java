package io.github.deplague.jmcmcp.domain.model;

import java.time.Instant;
import java.util.Map;

public record HeapDumpInfo(
        String heapDumpId,
        String fileName,
        long fileSize,
        Instant uploadTime,
        String format,
        long objectCount,
        long classCount,
        Map<String, Object> extra
) {}
