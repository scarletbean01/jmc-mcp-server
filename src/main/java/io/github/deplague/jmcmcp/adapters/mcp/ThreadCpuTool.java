package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.ThreadCpuApplicationService;
import io.github.deplague.jmcmcp.domain.model.ThreadCpuResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * MCP tool adapter for thread CPU analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class ThreadCpuTool implements McpTool {

    private static final String NAME = "thread_cpu";

    private final ThreadCpuApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Identify which threads are consuming the most CPU based on execution samples.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "package_prefix", SchemaUtil.stringProp(
                                                "Optional package prefix to filter stack traces (e.g., 'com.mycompany')"),
                                        "top_n", SchemaUtil.intProp(
                                                "Number of top hot threads to return (default 10)", 10)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        String packagePrefix = SchemaUtil.getStringOrDefault(request.arguments(), "package_prefix", null);
                        int topN = SchemaUtil.getIntOrDefault(request.arguments(), "top_n", 10);

                        ThreadCpuResult result = appService.analyze(filePath, startTimeStr, endTimeStr, packagePrefix, topN);
                        String markdown = formatMarkdown(result);
                        return CallToolResult.builder().addTextContent(markdown).isError(false).build();
                    } catch (Exception e) {
                        return CallToolResult.builder().addTextContent("Error: " + e.getMessage()).isError(true).build();
                    }
                })
                .build();
    }

    private String formatMarkdown(ThreadCpuResult result) {
        if (result.totalSamples() == 0) {
            return "# Thread CPU Analysis\n\nNo execution samples found in the recording.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Thread CPU Analysis\n\n");
        sb.append("Total samples: ").append(result.totalSamples()).append("\n\n");

        sb.append("## Per-Thread CPU Summary\n\n");
        sb.append("| Thread Name | Samples | CPU % | Primary States |\n");
        sb.append("|-------------|---------|-------|----------------|\n");
        for (var thread : result.threads()) {
            String states = thread.stateCounts().entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(2)
                    .map(e -> String.format("%s (%d)", e.getKey(), e.getValue()))
                    .collect(Collectors.joining(", "));
            sb.append(String.format("| %s | %d | %.2f%% | %s |%n",
                    thread.threadName(), thread.samples(), thread.cpuPercent(), states));
        }

        sb.append("\n## Top Methods per Thread\n\n");
        for (var thread : result.threads()) {
            sb.append("### ").append(thread.threadName()).append("\n\n");
            sb.append("| Samples | Method (Top Frame) |\n");
            sb.append("|---------|--------------------|\n");
            for (var method : thread.topMethods()) {
                sb.append(String.format("| %d | `%s` |%n", method.samples(), method.method()));
            }
            sb.append("\n");
        }

        sb.append("## Thread State Distribution\n\n");
        sb.append("| State | Samples | Percentage |\n");
        sb.append("|-------|---------|------------|\n");
        for (var state : result.stateDistribution()) {
            sb.append(String.format("| %s | %d | %.2f%% |%n", state.state(), state.samples(), state.percent()));
        }

        return sb.toString();
    }
}
