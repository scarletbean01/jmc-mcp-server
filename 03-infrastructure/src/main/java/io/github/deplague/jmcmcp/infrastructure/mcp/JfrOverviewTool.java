package io.github.deplague.jmcmcp.infrastructure.mcp;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolOutput;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolDocs;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolNames;

import io.github.deplague.jmcmcp.application.service.JfrOverviewApplicationService;
import io.github.deplague.jmcmcp.domain.model.JfrOverviewResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for JFR recording overview.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class JfrOverviewTool {

    private final JfrOverviewApplicationService appService;

    @RunOnVirtualThread
    @Tool(
            name = ToolNames.JFR_OVERVIEW,
            title = "JFR Recording Overview",
            description = "Provide a high-level overview of a JFR recording, including recording duration and event counts.",
            annotations = @Tool.Annotations(readOnlyHint = true, idempotentHint = true, openWorldHint = false)
    )
    public ToolResponse jfrOverview(
            @ToolArg(name = "jfr_file_path", description = ToolDocs.JFR_FILE_PATH) String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = ToolDocs.START_TIME) String startTime,
            @ToolArg(name = "end_time", required = false, description = ToolDocs.END_TIME) String endTime
    ) throws Exception {
        TimeRangeArgs.validateRange(startTime, endTime);
                JfrOverviewResult result = appService.analyze(
                            jfrFilePath,
                            startTime,
                            endTime
                    );
                    String markdown = formatMarkdown(result);
                    return ToolResponse.success(ToolOutput.limit(markdown));
    }

    private String formatMarkdown(JfrOverviewResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# JFR Recording Overview\n\n");
        sb.append("**File:** ").append(result.filePath()).append("\n");
        sb.append("**Duration:** ")
                .append(String.format("%.2f", result.durationSeconds()))
                .append(" seconds\n\n");

        sb.append("## Event Summary\n");
        sb.append(String.format(
                "- **Total Events (full file):** %d%n",
                result.totalEvents()
        ));

        if (result.filteredEvents() != null) {
            sb.append(String.format(
                    "- **Filtered Events:** %d%n",
                    result.filteredEvents()
            ));
        }

        return sb.toString();
    }
}
