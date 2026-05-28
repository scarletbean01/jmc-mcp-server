package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.ContainerMetricsApplicationService;
import io.github.deplague.jmcmcp.domain.model.ContainerMetricsResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for container resource limits and usage analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class ContainerMetricsTool implements McpTool {

    private static final String NAME = "container_metrics";

    private final ContainerMetricsApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description("Analyze container resource limits and usage (Docker/Kubernetes).")
                                .inputSchema(
                                        SchemaUtil.objectSchema(
                                                SchemaUtil.props(
                                                        "jfr_file_path",
                                                        SchemaUtil.jfrFileProp(),
                                                        "start_time",
                                                        SchemaUtil.startTimeProp(),
                                                        "end_time",
                                                        SchemaUtil.endTimeProp()
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

                        ContainerMetricsResult result = appService.analyze(
                                filePath,
                                startTimeStr,
                                endTimeStr
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

    private String formatMarkdown(ContainerMetricsResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Container Metrics Analysis\n\n");

        if (!result.hasData()) {
            sb.append(
                    "No container configuration or usage events found. The JVM is likely not running in a container, or container tracking is disabled."
            );
            return sb.toString();
        }

        result.config().ifPresent(cfg -> {
            sb.append("## Container Configuration\n");
            cfg.cpuShares().ifPresent(v -> sb.append("- **CPU Shares:** ").append(v).append("\n"));
            cfg.cpuPeriod().ifPresent(v -> sb.append("- **CPU Period:** ").append(v).append("\n"));
            cfg.cpuQuota().ifPresent(v -> sb.append("- **CPU Quota:** ").append(v).append("\n"));
            cfg.memoryLimit().ifPresent(v -> sb.append("- **Memory Limit:** ").append(v).append("\n"));
            cfg.swapLimit().ifPresent(v -> sb.append("- **Swap Limit:** ").append(v).append("\n"));
            cfg.memorySoftLimit().ifPresent(v -> sb.append("- **Memory Soft Limit:** ").append(v).append("\n"));
            sb.append("\n");
        });

        result.cpuUsage().ifPresent(cpu -> {
            sb.append("## Container CPU Usage\n");
            cpu.avgCpuTime().ifPresent(v -> sb.append("- **Avg CPU Time:** ").append(v).append("\n"));
            cpu.maxCpuTime().ifPresent(v -> sb.append("- **Max CPU Time:** ").append(v).append("\n\n"));
        });

        result.memoryUsage().ifPresent(mem -> {
            sb.append("## Container Memory Usage\n");
            mem.avgMemoryUsage().ifPresent(v -> sb.append("- **Avg Memory Usage:** ").append(v).append("\n"));
            mem.maxMemoryUsage().ifPresent(v -> sb.append("- **Max Memory Usage:** ").append(v).append("\n"));
            mem.avgSwapUsage().ifPresent(v -> sb.append("- **Avg Swap Usage:** ").append(v).append("\n"));
            mem.maxSwapUsage().ifPresent(v -> sb.append("- **Max Swap Usage:** ").append(v).append("\n\n"));
        });

        return sb.toString();
    }
}
