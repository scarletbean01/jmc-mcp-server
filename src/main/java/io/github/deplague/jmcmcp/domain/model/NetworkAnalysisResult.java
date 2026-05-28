package io.github.deplague.jmcmcp.domain.model;

import java.util.List;
import java.util.Optional;

/**
 * Result of network socket analysis.
 */
public record NetworkAnalysisResult(
        long connectCount,
        long readCount,
        long writeCount,
        Optional<String> avgConnectDuration,
        Optional<String> maxConnectDuration,
        Optional<String> p95ConnectDuration,
        List<NetworkConnectEntry> topConnections,
        List<NetworkReadEntry> topReads,
        List<NetworkWriteEntry> topWrites,
        List<NetworkLatencyPercentile> latencyPercentiles,
        boolean hasData
) {
}
