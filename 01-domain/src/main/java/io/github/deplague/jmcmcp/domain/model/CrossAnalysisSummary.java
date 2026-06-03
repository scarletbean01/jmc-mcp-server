package io.github.deplague.jmcmcp.domain.model;

public record CrossAnalysisSummary(
        int highSeverityCount,
        int mediumSeverityCount,
        int lowSeverityCount,
        int totalClasses
) {
}
