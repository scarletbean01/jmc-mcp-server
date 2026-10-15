package io.github.deplague.jmcmcp.infrastructure.mcp;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolOutput;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolDocs;
import io.github.deplague.jmcmcp.infrastructure.mcp.ToolNames;

import io.github.deplague.jmcmcp.application.service.JfrRulesApplicationService;
import io.github.deplague.jmcmcp.domain.model.JfrRulesResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for JMC rules engine analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class JfrRulesTool {

    private final JfrRulesApplicationService appService;

    @RunOnVirtualThread
    @Tool(
            name = ToolNames.JFR_RULES,
            title = "JFR Automated Rules Assessment",
            description = "Automatically detects performance issues using JMC's built-in rules engine.",
            annotations = @Tool.Annotations(readOnlyHint = true, idempotentHint = true, openWorldHint = false)
    )
    public ToolResponse jfrRules(
            @ToolArg(name = "jfr_file_path", description = ToolDocs.JFR_FILE_PATH) String jfrFilePath,
            @ToolArg(name = "start_time", required = false, description = ToolDocs.START_TIME) String startTime,
            @ToolArg(name = "end_time", required = false, description = ToolDocs.END_TIME) String endTime,
            @ToolArg(name = "min_severity", required = false, description = "Minimum rule severity threshold to include (OK, INFO, WARNING, IGNORE). Default is WARNING.") String minSeverity
    ) throws Exception {
        TimeRangeArgs.validateRange(startTime, endTime);
                JfrRulesResult result = appService.analyze(
                            jfrFilePath,
                            startTime,
                            endTime,
                            minSeverity != null ? minSeverity : "WARNING"
                    );
                    String markdown = formatMarkdown(result);
                    return ToolResponse.success(ToolOutput.limit(markdown));
    }

    private String formatMarkdown(JfrRulesResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# JMC Automated Bottleneck Detection\n\n");
        sb.append(String.format(
                "Rules with severity >= %s are shown.\n\n",
                result.minSeverity()
        ));

        if (!result.hasData()) {
            sb.append("No issues found exceeding the threshold.\n");
            return sb.toString();
        }

        for (var entry : result.rules()) {
            sb.append(String.format("### %s (%s)%n", entry.name(), entry.severity()));
            sb.append("**Summary:** ").append(entry.summary()).append("\n\n");
            if (entry.explanation() != null) {
                sb.append(entry.explanation()).append("\n\n");
            }
        }

        return sb.toString();
    }
}
