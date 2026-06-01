package io.github.deplague.jmcmcp.domain.model;

import java.util.Optional;

/**
 * Container memory usage statistics.
 */
public record ContainerMemoryUsage(
        Optional<String> avgMemoryUsage,
        Optional<String> maxMemoryUsage,
        Optional<String> avgSwapUsage,
        Optional<String> maxSwapUsage
) {
}
