package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.adapters.infrastructure.JfrProviderImpl;
import io.github.deplague.jmcmcp.application.service.GcRecommendationsApplicationService;
import io.github.deplague.jmcmcp.domain.service.GcRecommendationsService;
import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.security.RecordingAccessController;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;

/**
 * Legacy compatibility wrapper for tests that still rely on the
 * old {@code new GcRecommendationsTool(JfrAnalysisService)} API.
 * This class is NOT registered as an MCP tool.
 */
public final class GcRecommendationsTool {

    private final io.github.deplague.jmcmcp.adapters.mcp.GcRecommendationsTool adapter;

    public GcRecommendationsTool(JfrAnalysisService service) {
        JfrRecordingCache cache = service.getRecordingCache();
        RecordingAccessController accessController = new RecordingAccessController();
        JfrProviderImpl jfrProvider = new JfrProviderImpl(cache, accessController);
        GcRecommendationsService domainService = new GcRecommendationsService();
        GcRecommendationsApplicationService appService = new GcRecommendationsApplicationService(
                jfrProvider, domainService
        );
        this.adapter = new io.github.deplague.jmcmcp.adapters.mcp.GcRecommendationsTool(appService);
    }

    public SyncToolSpecification spec() {
        return adapter.spec();
    }
}
