package io.github.deplague.jmcmcp.domain.model;

import java.util.List;

public record CrossAnalysisResult(
        boolean hasData,
        List<UnifiedClassEntry> classes,
        CrossAnalysisSummary summary,
        String recommendation
) {
}
