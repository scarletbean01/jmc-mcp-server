package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.adapters.infrastructure.JfrProviderImpl;
import io.github.deplague.jmcmcp.application.service.JfrOverviewApplicationService;
import io.github.deplague.jmcmcp.domain.service.JfrOverviewService;
import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.security.RecordingAccessController;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;

/**
 * Legacy compatibility wrapper for tests that still rely on the
 * old {@code new JfrOverviewTool(JfrAnalysisService)} API.
 * This class is NOT registered as an MCP tool.
 */
public final class JfrOverviewTool {

    private final io.github.deplague.jmcmcp.adapters.mcp.JfrOverviewTool adapter;

    public JfrOverviewTool(JfrAnalysisService service) {
        JfrRecordingCache cache = service.getRecordingCache();
        RecordingAccessController accessController = new RecordingAccessController();
        JfrProviderImpl jfrProvider = new JfrProviderImpl(cache, accessController);
        JfrOverviewService domainService = new JfrOverviewService();
        JfrOverviewApplicationService appService = new JfrOverviewApplicationService(
                jfrProvider, domainService
        );
        this.adapter = new io.github.deplague.jmcmcp.adapters.mcp.JfrOverviewTool(appService);
    }

    public SyncToolSpecification spec() {
        return adapter.spec();
    }
}
