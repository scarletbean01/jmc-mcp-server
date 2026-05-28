package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.ProcessInfoApplicationService;
import io.github.deplague.jmcmcp.domain.model.ProcessEntry;
import io.github.deplague.jmcmcp.domain.model.ProcessInfoResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for OS and environment context analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class ProcessInfoTool implements McpTool {

    private static final String NAME = "process_info";

    private final ProcessInfoApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description(
                                        "Gather OS version, virtualization details, and running processes context."
                                )
                                .inputSchema(
                                        SchemaUtil.objectSchema(
                                                SchemaUtil.props(
                                                        "jfr_file_path",
                                                        SchemaUtil.jfrFileProp()
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

                        ProcessInfoResult result = appService.analyze(filePath);
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
