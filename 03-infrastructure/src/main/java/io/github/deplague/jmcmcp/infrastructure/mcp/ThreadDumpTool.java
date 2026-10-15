package io.github.deplague.jmcmcp.infrastructure.mcp;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolOutput;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolDocs;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolNames;

import io.github.deplague.jmcmcp.application.service.ThreadDumpApplicationService;
import io.github.deplague.jmcmcp.domain.model.ThreadDumpResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for extracting periodic thread dumps from JFR recordings.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class ThreadDumpTool {

    private final ThreadDumpApplicationService appService;

    @RunOnVirtualThread
    @Tool(
            name = ToolNames.THREAD_DUMP,
            title = "Thread Dump Analysis",
            description = "Extract periodic thread dumps from a JFR recording. Returns the text of thread dumps captured at various points during the recording.",
            annotations = @Tool.Annotations(readOnlyHint = true, idempotentHint = true, openWorldHint = false)
    )
    public ToolResponse threadDumps(
            @ToolArg(name = "jfr_file_path", description = ToolDocs.JFR_FILE_PATH) String filePath,
            @ToolArg(name = "max_dumps", description = "Maximum number of thread dumps to return", required = false) Integer maxDumps) throws Exception {
        if (maxDumps == null) {
                        maxDumps = 5;
                    }
                    ThreadDumpResult result = appService.analyze(filePath, maxDumps);
                    return ToolResponse.success(ToolOutput.limit(formatMarkdown(result)));
    }

    private String formatMarkdown(ThreadDumpResult result) {
        if (!result.hasData()) {
            return "No periodic thread dumps found in this recording. "
                    + "Ensure 'Thread Dump' events were enabled in the recording settings.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Thread Dumps\n\n");

        for (var dump : result.dumps()) {
            sb.append("## Dump at ").append(dump.timestamp()).append("\n\n");
            sb.append("```\n");
            sb.append(dump.content());
            sb.append("\n```\n\n");
        }

        return sb.toString();
    }
}
