package io.github.deplague.jmcmcp.domain.model;

/**
 * Represents a single hot method entry within a CPU flame analysis.
 */
public record CpuFlameMethodEntry(String methodName, long samples, double percentage) {
}
