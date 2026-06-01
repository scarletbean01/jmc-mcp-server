package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

/**
 * Structured result of cross-dimensional correlation analysis.
 */
public record CorrelateResult(
        List<CorrelateLockSite> lockSites,
        List<CorrelateIoSite> ioSites,
        List<CorrelateHotMethod> hotMethods,
        CpuGcMetrics cpuGcMetrics,
        boolean showLockIo,
        boolean showCpuGc,
        int topN
) {
}
