package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.IoAnalysisApplicationService;
import io.github.deplague.jmcmcp.domain.model.IoAnalysisResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for file and socket I/O analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class IoAnalysisTool implements McpTool {

    private static final String NAME = "io_analysis";

    private final IoAnalysisApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description(
                                        "Analyze file and socket I/O events in a JFR recording. "
                                                + "Reports read/write durations and throughput."
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
                                                        "io_type",
                                                        SchemaUtil.stringProp(
                                                                "Which I/O events to analyze: file, socket, or all (default)",
                                                                java.util.List.of("file", "socket", "all")
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
                        String ioType = SchemaUtil.getStringOrDefault(
                                request.arguments(),
                                "io_type",
                                "all"
                        );

                        IoAnalysisResult result = appService.analyze(
                                filePath,
                                startTimeStr,
                                endTimeStr,
                                ioType
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

    private String formatMarkdown(IoAnalysisResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# I/O Analysis\n\n");

        if (!result.hasData()) {
            sb.append("No I/O events found in this recording range.\n");
            return sb.toString();
        }

        result.fileIo().ifPresent(io -> {
            sb.append("## File I/O\n");
            io.eventCount().ifPresent(v -> sb.append(String.format("- **Event Count:** %s%n", v)));
            io.totalDuration().ifPresent(v -> sb.append(String.format("- **Total Duration:** %s%n", v)));
            io.avgDuration().ifPresent(v -> sb.append(String.format("- **Average Duration:** %s%n", v)));
            io.totalRead().ifPresent(v -> sb.append(String.format("- **Total Read:** %s%n", v)));
            io.totalWrite().ifPresent(v -> sb.append(String.format("- **Total Written:** %s%n", v)));
            sb.append("\n");
        });

        result.socketIo().ifPresent(io -> {
            sb.append("## Socket I/O\n");
            io.eventCount().ifPresent(v -> sb.append(String.format("- **Event Count:** %s%n", v)));
            io.totalDuration().ifPresent(v -> sb.append(String.format("- **Total Duration:** %s%n", v)));
            io.avgDuration().ifPresent(v -> sb.append(String.format("- **Average Duration:** %s%n", v)));
            io.totalRead().ifPresent(v -> sb.append(String.format("- **Total Read:** %s%n", v)));
            io.totalWrite().ifPresent(v -> sb.append(String.format("- **Total Written:** %s%n", v)));
            sb.append("\n");
        });

        return sb.toString();
    }
}
