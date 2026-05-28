package io.github.deplague.jmcmcp.domain.model;

import java.util.Optional;

/**
 * Container CPU usage statistics.
 */
public record ContainerCpuUsage(
        Optional<String> avgCpuTime,
        Optional<String> maxCpuTime
) {
}
