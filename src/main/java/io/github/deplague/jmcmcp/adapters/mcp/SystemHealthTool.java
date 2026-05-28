package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.SystemHealthApplicationService;
import io.github.deplague.jmcmcp.domain.model.SystemHealthResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for system health analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class SystemHealthTool implements McpTool {

    private static final String NAME = "system_health";

    private final SystemHealthApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze system health metrics from a JFR recording, including CPU load, physical memory usage, and swap usage.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.commonJfrProps(),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);

                        SystemHealthResult result = appService.analyze(filePath, startTimeStr, endTimeStr);
                        String markdown = formatMarkdown(result);
                        return CallToolResult.builder().addTextContent(markdown).isError(false).build();
                    } catch (Exception e) {
                        return CallToolResult.builder().addTextContent("Error: " + e.getMessage()).isError(true).build();
                    }
                })
                .build();
    }

    private String formatMarkdown(SystemHealthResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# System Health Analysis\n\n");

        boolean highCpuDetected = result.highCpuDetected();

        result.cpuLoad().ifPresent(cpu -> {
            sb.append("## CPU Load\n");
            if (cpu.avgMachineTotal() != null) sb.append(String.format("- **Avg Machine Total:** %s%n", cpu.avgMachineTotal()));
            if (cpu.maxMachineTotal() != null) sb.append(String.format("- **Max Machine Total:** %s%n", cpu.maxMachineTotal()));
            if (cpu.avgJvmUser() != null) sb.append(String.format("- **Avg JVM User:** %s%n", cpu.avgJvmUser()));
            if (cpu.avgJvmSystem() != null) sb.append(String.format("- **Avg JVM System:** %s%n", cpu.avgJvmSystem()));
            sb.append("\n");
        });

        result.physicalMemory().ifPresent(mem -> {
            sb.append("## Physical Memory\n");
            sb.append(String.format("- **Total Physical Memory:** %s%n", mem.totalSize()));
            sb.append(String.format("- **Min Used:** %s%n", mem.minUsed()));
            sb.append(String.format("- **Max Used:** %s%n", mem.maxUsed()));
            sb.append(String.format("- **Avg Used:** %s%n", mem.avgUsed()));
            sb.append("\n");
        });

        result.cpuInfo().ifPresent(info -> {
            sb.append("## CPU Information\n");
            if (info.cpu() != null) sb.append(String.format("- **CPU:** %s%n", info.cpu()));
            if (info.cores() > 0) sb.append(String.format("- **Cores:** %d%n", info.cores()));
            if (info.sockets() > 0) sb.append(String.format("- **Sockets:** %d%n", info.sockets()));
            sb.append("\n");
        });

        result.containerConfig().ifPresent(config -> {
            sb.append("## Container Configuration\n");
            sb.append("- **CPU Shares:** ").append(config.cpuShares()).append("\n");
            sb.append("- **CPU Period:** ").append(config.cpuPeriod()).append("\n");
            sb.append("- **CPU Quota:** ").append(config.cpuQuota()).append("\n");
            sb.append("- **Memory Limit:** ").append(config.memoryLimit()).append("\n");
            sb.append("- **Swap Limit:** ").append(config.swapLimit()).append("\n");
            sb.append("\n");
        });

        if (!result.hasData()) {
            sb.append("No system health events found in this recording.\n");
        }

        if (highCpuDetected) {
            sb.append("\n<agent_hint>High CPU load detected. Use 'thread_cpu' or 'hot_methods' to identify the culprit. Or use the new 'diagnose_high_cpu' macro tool for an automated analysis.</agent_hint>\n");
        }

        return sb.toString();
    }
}
