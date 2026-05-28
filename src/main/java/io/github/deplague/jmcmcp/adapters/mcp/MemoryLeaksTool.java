package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.MemoryLeaksApplicationService;
import io.github.deplague.jmcmcp.domain.model.LeakSiteEntry;
import io.github.deplague.jmcmcp.domain.model.LeakingClassEntry;
import io.github.deplague.jmcmcp.domain.model.MemoryLeaksResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for memory leak analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class MemoryLeaksTool implements McpTool {

    private static final String NAME = "memory_leaks";

    private final MemoryLeaksApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description(
                                        "Analyze old object samples to identify potential memory leaks, "
                                                + "leaking classes, and allocation sites."
                                )
                                .inputSchema(
                                        SchemaUtil.objectSchema(
                                                SchemaUtil.props(
                                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                                        "start_time", SchemaUtil.startTimeProp(),
                                                        "end_time", SchemaUtil.endTimeProp(),
                                                        "top_n", SchemaUtil.intProp(
                                                                "Number of top leaking classes/sites (default 20)",
                                                                20
                                                        )
                                                ),
                                                SchemaUtil.required("jfr_file_path")
                                        )
                                )
                                .build()
                )
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        int topN = SchemaUtil.getIntOrDefault(request.arguments(), "top_n", 20);

                        MemoryLeaksResult result = appService.analyze(
                                filePath, startTimeStr, endTimeStr, topN
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

    private String formatMarkdown(MemoryLeaksResult result) {
        if (!result.hasData()) {
            return "# Memory Leaks Analysis\n\nNo jdk.OldObjectSample events found. "
                    + "Make sure you are using a profile that enables Old Object Sample events "
                    + "(e.g., -XX:StartFlightRecording:settings=profile).";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Memory Leaks Analysis (Old Object Samples)\n\n");
        sb.append("- **Total Sampled Objects:** ").append(result.totalSampledObjects()).append("\n\n");

        sb.append("## Top Leaking Classes\n");
        sb.append("| Class | Sample Count |\n|---|---|\n");
        for (LeakingClassEntry e : result.leakingClasses()) {
            sb.append(String.format("| `%s` | %d |\n", e.className(), e.sampleCount()));
        }
        sb.append("\n");

        sb.append("## Top Leak Allocation Sites\n");
        sb.append("| Sample Count | Allocation Site |\n|---|---|\n");
        for (LeakSiteEntry e : result.leakSites()) {
            sb.append(String.format(
                    "| %d | `%s` |\n",
                    e.sampleCount(),
                    e.siteKey().replace("\n", "`<br>`")
            ));
        }

        sb.append("\n<agent_hint>Leak suspects identified. Consider `predictive_leak_analysis` "
                + "for mathematical leak confirmation and OOM time projection, or `heap_trends` "
                + "for memory growth visualization.</agent_hint>\n");

        return sb.toString();
    }
}
