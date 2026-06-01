package io.github.deplague.jmcmcp.domain.model;

/**
 * CPU information.
 */
public record CpuInfo(
        String cpu,
        int cores,
        int sockets
) {
}
