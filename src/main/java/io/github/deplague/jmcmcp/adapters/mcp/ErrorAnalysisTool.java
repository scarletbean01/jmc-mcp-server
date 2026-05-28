package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.ErrorAnalysisApplicationService;
import io.github.deplague.jmcmcp.domain.model.ErrorAnalysisResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for error analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class ErrorAnalysisTool implements McpTool {

    private static final String NAME = "error_analysis";

    private final ErrorAnalysisApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze Java errors (OutOfMemoryError, StackOverflowError, InternalError, etc.) in a JFR recording. " +
                                "Reports top errors by class, message, and throw site with severity classification.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top errors to return (default 10)", 10)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        int topN = SchemaUtil.getIntOrDefault(request.arguments(), "top_n", 10);

                        ErrorAnalysisResult result = appService.analyze(filePath, startTimeStr, endTimeStr, topN);
                        String markdown = formatMarkdown(result);
                        return CallToolResult.builder().addTextContent(markdown).isError(false).build();
                    } catch (Exception e) {
                        return CallToolResult.builder().addTextContent("Error: " + e.getMessage()).isError(true).build();
                    }
                })
                .build();
    }

    private String formatMarkdown(ErrorAnalysisResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Error Analysis\n\n");

        if (!result.hasData()) {
            sb.append("No Java error throw events found in the recording.");
            return sb.toString();
        }

        sb.append("## Error Summary\n\n");
        sb.append("| Metric | Value |\n");
        sb.append("|--------|-------|\n");
        sb.append("| Total Errors | ").append(result.totalErrors()).append(" |\n");
        sb.append("| Total Exceptions | ").append(result.totalExceptions()).append(" |\n");
        if (result.totalExceptions() > 0) {
            sb.append(String.format("| Error-to-Exception Ratio | %.2f%% |%n", (result.totalErrors() * 100.0) / result.totalExceptions()));
        }
        sb.append("\n");

        sb.append("## Top Errors by Class\n\n");
        sb.append("| Count | Error Class | Message | Throw Site (top 5 frames) | Severity |\n");
        sb.append("|------|-------------|---------|---------------------------|----------|\n");

        for (var entry : result.topErrors()) {
            sb.append("| ").append(entry.count()).append(" | ");
            sb.append("`").append(entry.className()).append("` | ");
            sb.append("`").append(entry.message().replace("\n", " ")).append("` | ");
            sb.append("`").append(entry.stackTrace().replace("\n", "`<br>`")).append("` | ");
            sb.append(entry.severity()).append(" |\n");
        }

        return sb.toString();
    }
}
