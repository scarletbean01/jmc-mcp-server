package io.github.deplague.jmcmcp;

import io.github.deplague.jmcmcp.adapters.mcp.McpTool;
import io.github.deplague.jmcmcp.async.AsyncJobService;
import io.github.deplague.jmcmcp.jfr.CallTreeCache;
import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.resources.JdkBugDatabaseResource;
import io.github.deplague.jmcmcp.security.RecordingAccessController;
import io.github.deplague.jmcmcp.tools.*;
import io.modelcontextprotocol.json.jackson3.JacksonMcpJsonMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.json.JsonMapper;

/**
 * MCP Server entry point for Java Mission Control / JFR analysis.
 *
 * <p>Communicates via stdio (stdin/stdout) using the Model Context Protocol.
 * Never writes to stdout — all logging goes to stderr via logback.</p>
 *
 * <p>Phase 1 refactoring: migrated to {@link QuarkusApplication} with CDI
 * discovery for refactored tool adapters. Legacy tools remain manually
 * wired until Phase 2.</p>
 */
@QuarkusMain
public final class JmcMcpServer implements QuarkusApplication {

    private static final Logger LOG = LoggerFactory.getLogger(
            JmcMcpServer.class
    );

    @Inject
    Instance<McpTool> mcpTools;

    @Inject
    JfrRecordingCache cache;

    @Inject
    RecordingAccessController accessController;

    @Inject
    AsyncJobService asyncJobService;

    @Inject
    CallTreeCache callTreeCache;

    public static void main(String[] args) {
        Quarkus.run(JmcMcpServer.class, args);
    }

    @Override
    public int run(String... args) throws Exception {
        LOG.info("Starting JMC MCP Server...");

        // Legacy analysis service for tools not yet refactored
        JfrAnalysisService analysisService = new JfrAnalysisService(
                cache,
                accessController,
                asyncJobService
        );

        // Create stdio transport provider with Jackson 3 JSON mapper
        JsonMapper jsonMapper = JsonMapper.builder().build();
        StdioServerTransportProvider transport =
                new StdioServerTransportProvider(
                        new JacksonMcpJsonMapper(jsonMapper)
                );

        // Build the sync MCP server
        McpSyncServer server = McpServer.sync(transport)
                .serverInfo("jmc-mcp", "1.0.0")
                .capabilities(
                        McpSchema.ServerCapabilities.builder()
                                .tools(true)
                                .resources(true, true)
                                .build()
                )
                .build();

        // Register resources
        server.addResource(new JdkBugDatabaseResource().spec());

        // Register CDI-discovered tools (refactored Phase-1 adapters)
        List<McpServerFeatures.SyncToolSpecification> tools =
                new ArrayList<>();
        for (McpTool tool : mcpTools) {
            tools.add(tool.spec());
            LOG.debug(
                    "Registered CDI tool: {}",
                    tool.spec().tool().name()
            );
        }

        // Register legacy tools (not yet refactored to adapters)
        tools.addAll(List.of(
                new IoHotspotsTool(analysisService).spec(),
                new ThreadActivityTool(analysisService).spec(),
                new GcAnalysisTool(analysisService).spec(),
                new ThreadContentionTool(analysisService).spec(),
                new LiveRecordingTool().spec(),
                new SystemHealthTool(analysisService).spec(),
                new TimeSeriesTool(analysisService).spec(),
                new CompareRecordingsTool(analysisService).spec(),
                new ErrorAnalysisTool(analysisService).spec(),
                new NetworkAnalysisTool(analysisService).spec(),
                new LockAnalysisTool(analysisService).spec(),
                new ThreadCpuTool(analysisService).spec(),
                new HighCpuDiagnosticTool(analysisService).spec(),
                new ThreadPoolAnalysisTool(analysisService).spec(),
                // Phase 1 new tools
                new StackTraceSearchTool(analysisService).spec(),
                new RequestWaterfallTool(analysisService).spec(),
                new CorrelateTool(analysisService).spec(),
                new QuickAnalysisTool(analysisService).spec(),
                new DiffStackTracesTool(analysisService).spec(),
                // Interactive call tree tools
                new CallTreeTool(analysisService, callTreeCache).spec(),
                new ExpandCallTreeTool(callTreeCache).spec(),
                new DiffCallTreeTool(analysisService, callTreeCache).spec(),
                new ExpandDiffCallTreeTool(callTreeCache).spec(),
                // Smart heuristic tools
                new SmartLockResolverTool(analysisService).spec(),
                new SmartThreadStarvationDetectorTool(analysisService).spec(),
                new SmartJdbcNPlusOneAnalyzerTool(analysisService).spec(),
                // Enterprise infrastructure tools
                new HealthCheckTool(cache, asyncJobService).spec(),
                new GetJobStatusTool(asyncJobService).spec(),
                new GetJobResultTool(asyncJobService).spec()
        ));

        for (var tool : tools) {
            server.addTool(tool);
            LOG.debug("Registered tool: {}", tool.tool().name());
        }

        LOG.info(
                "JMC MCP Server started with {} tools. Waiting for requests...",
                tools.size()
        );

        // Register shutdown hook for graceful cleanup of daemon-thread executors
        CountDownLatch shutdownLatch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    LOG.info("Shutting down JMC MCP Server...");
                    cache.shutdown();
                    callTreeCache.shutdown();
                    asyncJobService.shutdown();
                    shutdownLatch.countDown();
                })
        );

        // Block until shutdown signal to keep Quarkus alive
        shutdownLatch.await();
        return 0;
    }
}
