package io.github.deplague.jmcmcp.domain.model;

/**
 * GC configuration extracted from jdk.GCConfiguration, jdk.GCHeapConfiguration and jdk.GCSurvivorConfiguration.
 */
public record GcConfiguration(
        String youngCollector,
        String oldCollector,
        String parallelGcThreads,
        String concurrentGcThreads,
        String minHeapSize,
        String maxHeapSize,
        String initialHeapSize,
        String maxTenuringThreshold
) {
}
