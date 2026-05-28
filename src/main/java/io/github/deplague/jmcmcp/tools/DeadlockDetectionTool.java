package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.adapters.infrastructure.JfrProviderImpl;
import io.github.deplague.jmcmcp.application.service.DeadlockDetectionApplicationService;
import io.github.deplague.jmcmcp.domain.service.DeadlockDetectionService;
import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.security.RecordingAccessController;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;

/**
 * Legacy compatibility wrapper for tests that still rely on the
 * old {@code new DeadlockDetectionTool(JfrAnalysisService)} API.
 * This class is NOT registered as an MCP tool.
 */
public final class DeadlockDetectionTool {

    private final io.github.deplague.jmcmcp.adapters.mcp.DeadlockDetectionTool adapter;

    public DeadlockDetectionTool(JfrAnalysisService service) {
        JfrRecordingCache cache = new JfrRecordingCache();
        RecordingAccessController accessController = new RecordingAccessController();
        JfrProviderImpl jfrProvider = new JfrProviderImpl(cache, accessController);
        DeadlockDetectionService domainService = new DeadlockDetectionService();
        DeadlockDetectionApplicationService appService = new DeadlockDetectionApplicationService(
                jfrProvider, domainService
        );
        this.adapter = new io.github.deplague.jmcmcp.adapters.mcp.DeadlockDetectionTool(appService);
    }

    public SyncToolSpecification spec() {
        return adapter.spec();
    }
}
