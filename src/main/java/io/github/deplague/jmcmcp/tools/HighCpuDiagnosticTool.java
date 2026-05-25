package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;

import java.io.IOException;

/**
 * Macro tool for diagnosing high CPU usage.
 * Orchestrates SystemHealthTool, ThreadCpuTool, and HotMethodsTool.
 */
public final class HighCpuDiagnosticTool {

    private static final String NAME = "diagnose_high_cpu";

    private final JfrAnalysisService service;

    public HighCpuDiagnosticTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Macro tool: Automatically orchestrates system health, thread CPU, and hot methods to diagnose high CPU usage.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "package_prefix", SchemaUtil.stringProp("Optional package prefix to filter stack traces (e.g., 'com.mycompany')"),
                                        "async", SchemaUtil.boolProp("Run analysis asynchronously and return a job ID", false)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> service.execute(NAME, request.arguments(), () -> {
                    String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                    String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                    String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                    String packagePrefix = SchemaUtil.getStringOrDefault(request.arguments(), "package_prefix", null);
                    return analyze(filePath, startTimeStr, endTimeStr, packagePrefix);
                }))
                .build();
    }

    private String analyze(String filePath, String startTimeStr, String endTimeStr, String packagePrefix) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("# High CPU Diagnostic Report\n\n");
        sb.append("> **Macro Execution:** This report aggregates `system_health`, `thread_cpu`, and `hot_methods` into a single view.\n\n");

        sb.append("---\n");
        sb.append("## Step 1: System Health Context\n\n");
        SystemHealthTool systemHealthTool = new SystemHealthTool(service);
        try {
            String healthResult = systemHealthTool.analyze(filePath, startTimeStr, endTimeStr);
            sb.append(healthResult.replace("# System Health Analysis\n\n", ""));
        } catch (Exception e) {
            sb.append("Failed to gather system health: ").append(e.getMessage()).append("\n");
        }

        sb.append("\n---\n");
        sb.append("## Step 2: Top CPU Consuming Threads\n\n");
        ThreadCpuTool threadCpuTool = new ThreadCpuTool(service);
        try {
            String threadResult = threadCpuTool.analyze(filePath, startTimeStr, endTimeStr, packagePrefix, 5); // top 5 threads
            sb.append(threadResult.replace("# Thread CPU Analysis\n\n", ""));
        } catch (Exception e) {
            sb.append("Failed to gather thread CPU stats: ").append(e.getMessage()).append("\n");
        }

        sb.append("\n---\n");
        sb.append("## Step 3: Global Hot Methods\n\n");
        HotMethodsTool hotMethodsTool = new HotMethodsTool(service);
        try {
            String hotMethodsResult = hotMethodsTool.analyze(filePath, startTimeStr, endTimeStr, null, packagePrefix, 10); // top 10 methods overall
            sb.append(hotMethodsResult.replace("# Hot Methods & Call Paths\n\n", ""));
        } catch (Exception e) {
            sb.append("Failed to gather hot methods: ").append(e.getMessage()).append("\n");
        }

        sb.append("\n<agent_hint>Review the overarching CPU utilization in Step 1. Then match the top threads in Step 2 with the hot methods in Step 3. If standard CPU looks normal but the application is slow, consider investigating lock contention or I/O limits.</agent_hint>\n");

        return sb.toString();
    }
}