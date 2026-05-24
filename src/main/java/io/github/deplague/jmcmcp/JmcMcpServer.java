package io.github.deplague.jmcmcp;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.tools.*;
import io.modelcontextprotocol.json.jackson3.JacksonMcpJsonMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

/**
 * MCP Server entry point for Java Mission Control / JFR analysis.
 *
 * <p>Communicates via stdio (stdin/stdout) using the Model Context Protocol.
 * Never writes to stdout — all logging goes to stderr via logback.</p>
 */
public final class JmcMcpServer {

    private static final Logger LOG = LoggerFactory.getLogger(JmcMcpServer.class);

    public static void main(String[] args) {
        LOG.info("Starting JMC MCP Server...");

        JfrRecordingCache cache = new JfrRecordingCache();
        JfrAnalysisService analysisService = new JfrAnalysisService(cache);

        // Create stdio transport provider with Jackson 3 JSON mapper
        JsonMapper jsonMapper = JsonMapper.builder().build();
        StdioServerTransportProvider transport = new StdioServerTransportProvider(new JacksonMcpJsonMapper(jsonMapper));

        // Build the sync MCP server
        McpSyncServer server = McpServer.sync(transport).serverInfo("jmc-mcp", "1.0.0").capabilities(McpSchema.ServerCapabilities.builder().tools(true).build()).build();

        // Register all tools
        List<McpServerFeatures.SyncToolSpecification> tools = List
                .of(new JfrOverviewTool(analysisService).spec(),
                        new GcDetailTool(analysisService).spec(),
                        new IoHotspotsTool(analysisService).spec(),
                        new SafepointAnalysisTool(analysisService).spec(),
                        new ThreadActivityTool(analysisService).spec(),
                        new GcAnalysisTool(analysisService).spec(),
                        new HotMethodsTool(analysisService).spec(),
                        new ThreadContentionTool(analysisService).spec(),
                        new AllocationHotspotsTool(analysisService).spec(),
                        new IoAnalysisTool(analysisService).spec(),
                        new ExceptionAnalysisTool(analysisService).spec(),
                        new JfrRulesTool(analysisService).spec(),
                        new LiveRecordingTool().spec(),
                        new SystemHealthTool(analysisService).spec(),
                        new ThreadDumpTool(analysisService).spec(),
                        new SearchEventsTool(analysisService).spec(),
                        new VMOperationsTool(analysisService).spec(),
                        new ObjectStatisticsTool(analysisService).spec(),
                        new SystemPropertiesTool(analysisService).spec(),
                        new RecordingSettingsTool(analysisService).spec(),
                        new TimeSeriesTool(analysisService).spec(),
                        new JitCompilationTool(analysisService).spec(),
                        new ClassLoadingTool(analysisService).spec(),
                        new CompareRecordingsTool(analysisService).spec(),
                        new ErrorAnalysisTool(analysisService).spec(),
                        new HeapTrendsTool(analysisService).spec(),
                        new NetworkAnalysisTool(analysisService).spec(),
                        new EventSchemaTool(analysisService).spec(),
                        new NativeMemoryTool(analysisService).spec(),
                        new ClassHistogramTool(analysisService).spec(),
                        new CpuFlameTool(analysisService).spec(),
                        new JfrEventStatsTool(analysisService).spec(),
                        new MemoryLeaksTool(analysisService).spec(),
                        new LockAnalysisTool(analysisService).spec(),
                        new ContainerMetricsTool(analysisService).spec(),
                        new IncidentTimelineTool(analysisService).spec(),
                        new AllocationFlameTool(analysisService).spec(),
                        new LockFlameTool(analysisService).spec(),
                        new ThreadCpuTool(analysisService).spec(),
                        new BlockingSummaryTool(analysisService).spec(),
                        new VirtualThreadsTool(analysisService).spec(),
                        new GcCauseTool(analysisService).spec(),
                        new ThreadAllocationTool(analysisService).spec(),
                        new CodeCacheTool(analysisService).spec(),
                        new JvmFlagsTool(analysisService).spec(),
                        new DirectBuffersTool(analysisService).spec(),
                        new ProcessInfoTool(analysisService).spec(),
                        new HighCpuDiagnosticTool(analysisService).spec(),
                        new PredictiveLeakAnalysisTool(analysisService).spec(),
                        new DeadlockDetectionTool(analysisService).spec(),
                        new JdkBugReferenceTool(analysisService).spec(),
                        new GcRecommendationsTool(analysisService).spec(),
                        new ThreadPoolAnalysisTool(analysisService).spec());

        for (var tool : tools) {
            server.addTool(tool);
            LOG.debug("Registered tool: {}", tool.tool().name());
        }

        LOG.info("JMC MCP Server started with {} tools. Waiting for requests...", tools.size());

        // The transport handles the main loop; this call blocks until stdin closes.
        // No explicit shutdown hook needed for stdio transport.
    }
}
