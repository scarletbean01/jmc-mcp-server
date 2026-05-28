package io.github.deplague.jmcmcp.domain.model;

import java.util.List;
import java.util.Map;

/**
 * Per-thread CPU breakdown.
 */
public record ThreadCpuEntry(
        String threadName,
        long samples,
        double cpuPercent,
        Map<String, Long> stateCounts,
        List<MethodSampleEntry> topMethods
) {
}
