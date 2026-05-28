package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.adapters.infrastructure.JfrProviderImpl;
import io.github.deplague.jmcmcp.application.service.GcDetailApplicationService;
import io.github.deplague.jmcmcp.domain.service.GcDetailService;
import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.security.RecordingAccessController;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;

/**
 * Legacy compatibility wrapper for tests that still rely on the
 * old {@code new GcDetailTool(JfrAnalysisService)} API.
 * This class is NOT registered as an MCP tool.
 */
public final class GcDetailTool {

    private final io.github.deplague.jmcmcp.adapters.mcp.GcDetailTool adapter;

    public GcDetailTool(JfrAnalysisService service) {
        JfrRecordingCache cache = service.getRecordingCache();
        RecordingAccessController accessController = new RecordingAccessController();
        JfrProviderImpl jfrProvider = new JfrProviderImpl(cache, accessController);
        GcDetailService domainService = new GcDetailService();
        GcDetailApplicationService appService = new GcDetailApplicationService(
                jfrProvider, domainService
        );
        this.adapter = new io.github.deplague.jmcmcp.adapters.mcp.GcDetailTool(appService);
    }

    public SyncToolSpecification spec() {
        return adapter.spec();
    }
}
