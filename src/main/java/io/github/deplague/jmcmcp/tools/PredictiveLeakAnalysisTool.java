package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.adapters.infrastructure.JfrProviderImpl;
import io.github.deplague.jmcmcp.application.service.PredictiveLeakAnalysisApplicationService;
import io.github.deplague.jmcmcp.domain.service.PredictiveLeakAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.security.RecordingAccessController;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;

/**
 * Legacy compatibility wrapper for tests that still rely on the
 * old {@code new PredictiveLeakAnalysisTool(JfrAnalysisService)} API.
 * This class is NOT registered as an MCP tool.
 */
public final class PredictiveLeakAnalysisTool {

    private final io.github.deplague.jmcmcp.adapters.mcp.PredictiveLeakAnalysisTool adapter;

    public PredictiveLeakAnalysisTool(JfrAnalysisService service) {
        JfrRecordingCache cache = service.getRecordingCache();
        RecordingAccessController accessController = new RecordingAccessController();
        JfrProviderImpl jfrProvider = new JfrProviderImpl(cache, accessController);
        PredictiveLeakAnalysisService domainService = new PredictiveLeakAnalysisService();
        PredictiveLeakAnalysisApplicationService appService = new PredictiveLeakAnalysisApplicationService(
                jfrProvider, domainService
        );
        this.adapter = new io.github.deplague.jmcmcp.adapters.mcp.PredictiveLeakAnalysisTool(appService);
    }

    public SyncToolSpecification spec() {
        return adapter.spec();
    }
}
