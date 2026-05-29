package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.application.service.ProcessInfoApplicationService;
import io.github.deplague.jmcmcp.domain.model.ProcessEntry;
import io.github.deplague.jmcmcp.domain.model.ProcessInfoResult;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for OS and environment context analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@HandleToolError
@ApplicationScoped
public final class ProcessInfoTool {

    private final ProcessInfoApplicationService appService;

    @RunOnVirtualThread
    @Tool(description = "Gather OS version, virtualization details, and running processes context.")
    public ToolResponse processInfo(
            @ToolArg(name = "jfr_file_path", description = "Absolute or relative path to the .jfr recording file") String jfrFilePath
    ) {
        try {
            ProcessInfoResult result = appService.analyze(jfrFilePath);
            String markdown = formatMarkdown(result);
            return ToolResponse.success(markdown);
        } catch (Exception e) {
            return ToolResponse.error("Error: " + e.getMessage());
        }
    }

    private String formatMarkdown(ProcessInfoResult result) {
        if (!result.hasAnyInfo()) {
            return "# OS & Environment Context\n\nNo environment context events found in the recording.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# OS & Environment Context\n\n");

        if (result.osName().isPresent() || result.osVersion().isPresent() || result.osArch().isPresent()) {
            sb.append("## Operating System\n\n");
            result.osName().ifPresent(v -> sb.append("- **OS Name:** ").append(v).append("\n"));
            result.osVersion().ifPresent(v -> sb.append("- **OS Version:** ").append(v).append("\n"));
            result.osArch().ifPresent(v -> sb.append("- **Architecture:** ").append(v).append("\n"));
            sb.append("\n");
        }

        sb.append("## Virtualization\n\n");
        if (result.virtualizationTechnology().isPresent()) {
            sb.append("- **Technology:** ").append(result.virtualizationTechnology().get()).append("\n");
        } else {
            sb.append("- **Technology:** Unknown or Bare Metal\n");
        }
        sb.append("\n");

        if (!result.processes().isEmpty()) {
            sb.append("## Running Processes (Top 50)\n\n");
            sb.append("| PID | Command Line |\n");
            sb.append("|-----|--------------|\n");
            for (ProcessEntry p : result.processes()) {
                String cmd = p.command();
                if (cmd.length() > 200) {
                    cmd = cmd.substring(0, 200) + "...";
                }
                sb.append("| ").append(p.pid()).append(" | `").append(cmd).append("` |\n");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
