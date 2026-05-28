package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.ThreadAllocationApplicationService;
import io.github.deplague.jmcmcp.domain.model.ThreadAllocationResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for per-thread allocation breakdown.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class ThreadAllocationTool implements McpTool {

    private static final String NAME = "thread_allocation";

    private final ThreadAllocationApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description(
                                        "Identify which threads are allocating the most memory based on thread allocation statistics."
                                )
                                .inputSchema(
                                        SchemaUtil.objectSchema(
                                                SchemaUtil.props(
                                                        "jfr_file_path",
                                                        SchemaUtil.jfrFileProp(),
                                                        "start_time",
                                                        SchemaUtil.startTimeProp(),
                                                        "end_time",
                                                        SchemaUtil.endTimeProp(),
                                                        "top_n",
                                                        SchemaUtil.intProp(
                                                                "Number of top hot threads to return (default 10)",
                                                                10
                                                        )
                                                ),
                                                SchemaUtil.required("jfr_file_path")
                                        )
                                )
                                .build()
                )
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(
                                request.arguments(),
                                "jfr_file_path"
                        );
                        String startTimeStr = SchemaUtil.getStringOrDefault(
                                request.arguments(),
                                "start_time",
                                null
                        );
                        String endTimeStr = SchemaUtil.getStringOrDefault(
                                request.arguments(),
                                "end_time",
                                null
                        );
                        int topN = SchemaUtil.getIntOrDefault(
                                request.arguments(),
                                "top_n",
                                10
                        );

                        ThreadAllocationResult result = appService.analyze(
                                filePath,
                                startTimeStr,
                                endTimeStr,
                                topN
                        );
                        String markdown = formatMarkdown(result);
                        return CallToolResult.builder()
                                .addTextContent(markdown)
                                .isError(false)
                                .build();
                    } catch (Exception e) {
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
                                .build();
                    }
                })
                .build();
    }

    private String formatMarkdown(ThreadAllocationResult result) {
        if (!result.hasData()) {
            return "# Thread Allocation Analysis\n\nNo thread allocation statistics found in the recording. JFR event 'jdk.ThreadAllocationStatistics' may not be enabled.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Thread Allocation Analysis\n\n");

        sb.append("## Top Allocating Threads\n\n");
        sb.append("| Thread Name | Total Allocated (Estimated) | Allocation Rate |\n");
        sb.append("|-------------|-----------------------------|-----------------|\n");

        for (var entry : result.entries()) {
            sb.append("| ").append(entry.threadName()).append(" | ")
                    .append(entry.totalAllocated()).append(" | ")
                    .append(entry.allocationRate()).append(" |\n");
        }

        if (result.heavyAllocationDetected()) {
            sb.append(
                    "\n<agent_hint>High allocation detected. Use 'allocation_hotspots' or 'allocation_flame' with an optional 'package_prefix' to find the exact classes and call paths responsible for this memory pressure.</agent_hint>\n"
            );
        }

        return sb.toString();
    }
}
