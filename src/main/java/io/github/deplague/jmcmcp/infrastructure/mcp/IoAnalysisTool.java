package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.IoAnalysisApplicationService;
import io.github.deplague.jmcmcp.domain.model.IoAnalysisResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.github.deplague.jmcmcp.infrastructure.mcp.HandleToolError;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for file and socket I/O analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class IoAnalysisTool {

    private final IoAnalysisApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Analyze file and socket I/O events in a JFR recording. Reports read/write durations and throughput.")
    public ToolResponse ioAnalysis(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = "Optional start time in ISO-8601 format") String startTime,
            @ToolArg(name = "end_time", required = false, description = "Optional end time in ISO-8601 format") String endTime,
            @ToolArg(name = "io_type", required = false, description = "Which I/O events to analyze: file, socket, or all (default)") String ioType
    ) {
        try {
            IoAnalysisResult result = appService.analyze(
                    jfrFilePath,
                    startTime,
                    endTime,
                    ioType != null ? ioType : "all"
            );
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
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
