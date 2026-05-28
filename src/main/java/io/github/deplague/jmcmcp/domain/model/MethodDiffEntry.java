package io.github.deplague.jmcmcp.domain.model;

/**
 * Represents the diff of a single method between two recordings.
 */
public record MethodDiffEntry(
        String methodName,
        double baselineRate,
        double targetRate,
        double absoluteChange,
        double pctChange
) {
}
