package io.github.deplague.jmcmcp.domain.model;

/**
 * Represents a hot method identified from execution samples.
 */
public record CorrelateHotMethod(String methodName, long sampleCount) {
}
