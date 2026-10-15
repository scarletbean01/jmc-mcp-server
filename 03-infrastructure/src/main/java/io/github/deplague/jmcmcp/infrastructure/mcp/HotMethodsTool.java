package io.github.deplague.jmcmcp.infrastructure.mcp;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolOutput;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolDocs;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolNames;

import io.github.deplague.jmcmcp.application.service.HotMethodsApplicationService;
import io.github.deplague.jmcmcp.domain.model.HotMethodEntry;
import io.github.deplague.jmcmcp.domain.model.HotMethodsResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * MCP tool adapter for identifying hot methods from execution samples.
 * Delegates analysis to the application layer and formats results as Markdown.
 */
@HandleToolError
@ApplicationScoped
public final class HotMethodsTool {

    private final HotMethodsApplicationService applicationService;

    @Inject
    public HotMethodsTool(HotMethodsApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @RunOnVirtualThread
    @Tool(
            name = ToolNames.HOT_METHODS,
            title = "Hot Methods Analysis",
            description = "Identify hot methods and call paths in a JFR recording based on execution samples.",
            annotations = @Tool.Annotations(readOnlyHint = true, idempotentHint = true, openWorldHint = false)
    )
    public ToolResponse hotMethods(
            @ToolArg(name = "jfr_file_path", description = ToolDocs.JFR_FILE_PATH) String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = ToolDocs.START_TIME) String startTime,
            @ToolArg(name = "end_time", required = false, description = ToolDocs.END_TIME) String endTime,
            @ToolArg(name = "thread_name", required = false, description = "Optional thread name to filter execution samples by") String threadName,
            @ToolArg(name = "package_prefix", required = false, description = ToolDocs.PACKAGE_PREFIX) String packagePrefix,
            @ToolArg(name = "top_n", required = false, defaultValue = "10", description = ToolDocs.TOP_N_10) Integer topN
    ) throws Exception {
        TimeRangeArgs.validateRange(startTime, endTime);
                HotMethodsResult result = applicationService.analyze(
                            jfrFilePath,
                            startTime,
                            endTime,
                            threadName,
                            packagePrefix,
                            topN != null ? topN : 10
                    );
        
                    String markdown = formatMarkdown(result);
                    return ToolResponse.success(ToolOutput.limit(markdown));
    }

    private String formatMarkdown(HotMethodsResult result) {
        if (!result.hasResults()) {
            return "# Hot Methods\n\nNo execution samples found in the recording.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Hot Methods & Call Paths").append("\n\n");
        sb.append("| Samples | Stack Trace (top 5 frames) |").append("\n");
        sb.append("|---------|----------------------------|").append("\n");

        for (HotMethodEntry entry : result.entries()) {
            sb.append("| ").append(entry.sampleCount()).append(" | ");
            sb.append("`")
                    .append(entry.stackTrace().replace("\n", "`<br>`"))
                    .append("` |\n");
        }

        String topMethod = result.topMethod() != null
                ? result.topMethod()
                : "unknown";
        sb.append("\n<agent_hint>Top hot method is `")
                .append(topMethod)
                .append(
                        "`. Consider `thread_cpu` to see which threads consume the most CPU, `stack_trace_search` with `class_pattern` to find all events involving this class, or `correlate` to see if this method is associated with lock contention or I/O.</agent_hint>\n"
                );

        return sb.toString();
    }
}
