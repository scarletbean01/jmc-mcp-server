package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.adapters.infrastructure.JfrProviderImpl;
import io.github.deplague.jmcmcp.application.service.JdkBugReferenceApplicationService;
import io.github.deplague.jmcmcp.domain.service.JdkBugReferenceService;
import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.security.RecordingAccessController;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;

/**
 * Legacy compatibility wrapper for tests that still rely on the
 * old {@code new JdkBugReferenceTool(JfrAnalysisService)} API.
 * This class is NOT registered as an MCP tool.
 */
public final class JdkBugReferenceTool {

    private final io.github.deplague.jmcmcp.adapters.mcp.JdkBugReferenceTool adapter;

    public JdkBugReferenceTool(JfrAnalysisService service) {
        JfrRecordingCache cache = new JfrRecordingCache();
        RecordingAccessController accessController = new RecordingAccessController();
        JfrProviderImpl jfrProvider = new JfrProviderImpl(cache, accessController);
        JdkBugReferenceService domainService = new JdkBugReferenceService();
        JdkBugReferenceApplicationService appService = new JdkBugReferenceApplicationService(
                jfrProvider, domainService
        );
        this.adapter = new io.github.deplague.jmcmcp.adapters.mcp.JdkBugReferenceTool(appService);
    }

    public SyncToolSpecification spec() {
        return adapter.spec();
    }
}
