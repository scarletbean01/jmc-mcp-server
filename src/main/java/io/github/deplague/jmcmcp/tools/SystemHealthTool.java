package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;

import java.io.IOException;
import java.util.Optional;

/**
 * MCP tool for system health analysis (CPU load, Physical Memory, etc.)
 */
public final class SystemHealthTool {

    private static final String NAME = "system_health";

    private final JfrAnalysisService service;

    public SystemHealthTool(JfrAnalysisService service) {
        this.service = service;
    }

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

                String cached = service.getCachedResult(filePath, NAME, request.arguments());

                if (cached != null) {
                    return CallToolResult.builder().addTextContent(cached).isError(false).build();
                }

                String result = analyze(filePath, startTimeStr, endTimeStr);
                service.cacheResult(filePath, NAME, request.arguments(), result);
                return CallToolResult.builder().addTextContent(result).isError(false).build();
            } catch (Exception e) {
                return CallToolResult.builder().addTextContent("Error: " + e.getMessage()).isError(true).build();
            }
        }).build();
    }

    public String analyze(String filePath, String startTimeStr, String endTimeStr) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);
        StringBuilder sb = new StringBuilder();
        sb.append("# System Health Analysis\n\n");

        boolean highCpuDetected = false;

        // CPU Load
        var cpuLoad = events.apply(ItemFilters.type("jdk.CPULoad"));
        if (cpuLoad.hasItems()) {
            sb.append("## CPU Load\n");
            IQuantity avgMachineTotal = JfrItemUtils.avgQuantity(cpuLoad, "machineTotal");
            IQuantity maxMachineTotal = JfrItemUtils.maxQuantity(cpuLoad, "machineTotal");
            IQuantity avgJvmUser = JfrItemUtils.avgQuantity(cpuLoad, "jvmUser");
            IQuantity avgJvmSystem = JfrItemUtils.avgQuantity(cpuLoad, "jvmSystem");

            if (avgMachineTotal != null) {
                double val = avgMachineTotal.doubleValue();
                if (val > 0.8) highCpuDetected = true;
                sb.append(String.format("- **Avg Machine Total:** %.2f%%%n", val * 100));
            }
            if (maxMachineTotal != null) {
                double val = maxMachineTotal.doubleValue();
                if (val > 0.9) highCpuDetected = true;
                sb.append(String.format("- **Max Machine Total:** %.2f%%%n", val * 100));
            }
            if (avgJvmUser != null)
                sb.append(String.format("- **Avg JVM User:** %.2f%%%n", avgJvmUser.doubleValue() * 100));
            if (avgJvmSystem != null)
                sb.append(String.format("- **Avg JVM System:** %.2f%%%n", avgJvmSystem.doubleValue() * 100));
            sb.append("\n");
        }

        // Physical Memory
        var physicalMem = events.apply(ItemFilters.type("jdk.PhysicalMemory"));
        if (physicalMem.hasItems()) {
            sb.append("## Physical Memory\n");
            IQuantity totalSize = JfrItemUtils.maxQuantity(physicalMem, "totalSize");
            IQuantity minUsed = JfrItemUtils.minQuantity(physicalMem, "usedSize");
            IQuantity maxUsed = JfrItemUtils.maxQuantity(physicalMem, "usedSize");
            IQuantity avgUsed = JfrItemUtils.avgQuantity(physicalMem, "usedSize");

            sb.append(String.format("- **Total Physical Memory:** %s%n", JfrAnalysisService.display(totalSize)));
            sb.append(String.format("- **Min Used:** %s%n", JfrAnalysisService.display(minUsed)));
            sb.append(String.format("- **Max Used:** %s%n", JfrAnalysisService.display(maxUsed)));
            sb.append(String.format("- **Avg Used:** %s%n", JfrAnalysisService.display(avgUsed)));
            sb.append("\n");
        }

        // OS Information
        var cpuInfo = events.apply(ItemFilters.type("jdk.CPUInformation"));
        if (cpuInfo.hasItems()) {
            sb.append("## CPU Information\n");

            // Get first available item safely
            Optional<IItem> firstItem = cpuInfo.stream().flatMap(IItemIterable::stream).findFirst();

            firstItem.flatMap(item -> JfrItemUtils.getMember(item, "cpu")).ifPresent(cpuName -> sb.append(String.format("- **CPU:** %s%n", cpuName)));

            IQuantity cores = JfrItemUtils.maxQuantity(cpuInfo, "cores");
            IQuantity sockets = JfrItemUtils.maxQuantity(cpuInfo, "sockets");

            if (cores != null) sb.append(String.format("- **Cores:** %.0f%n", cores.doubleValue()));
            if (sockets != null) sb.append(String.format("- **Sockets:** %.0f%n", sockets.doubleValue()));
            sb.append("\n");
        }

        // Container Metrics Fallback
        var containerConfig = events.apply(ItemFilters.type("jdk.ContainerConfiguration"));
        if (containerConfig.hasItems()) {
            sb.append("## Container Configuration\n");
            Optional<IItem> itemOpt = containerConfig.stream().flatMap(IItemIterable::stream).findFirst();
            itemOpt.ifPresent(item -> {
                sb.append("- **CPU Shares:** ").append(JfrItemUtils.getMember(item, "cpuShares").orElse("N/A")).append("\n");
                sb.append("- **CPU Period:** ").append(JfrAnalysisService.display(JfrItemUtils.getQuantity(item, "cpuPeriod").orElse(null))).append("\n");
                sb.append("- **CPU Quota:** ").append(JfrAnalysisService.display(JfrItemUtils.getQuantity(item, "cpuQuota").orElse(null))).append("\n");
                sb.append("- **Memory Limit:** ").append(JfrAnalysisService.display(JfrItemUtils.getQuantity(item, "memoryLimit").orElse(null))).append("\n");
                sb.append("- **Swap Limit:** ").append(JfrAnalysisService.display(JfrItemUtils.getQuantity(item, "swapLimit").orElse(null))).append("\n");
            });
            sb.append("\n");
        }

        if (!cpuLoad.hasItems() && !physicalMem.hasItems() && !cpuInfo.hasItems() && !containerConfig.hasItems()) {
            sb.append("No system health events found in this recording.\n");
        }

        if (highCpuDetected) {
            sb.append("\n<agent_hint>High CPU load detected. Use 'thread_cpu' or 'hot_methods' to identify the culprit. Or use the new 'diagnose_high_cpu' macro tool for an automated analysis.</agent_hint>\n");
        }

        return sb.toString();
    }
}
