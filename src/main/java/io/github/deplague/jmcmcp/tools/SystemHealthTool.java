package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.ItemFilters;

import java.io.IOException;
import java.util.Map;
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
                        .description("Analyze system health metrics from a JFR recording, " +
                                "including CPU load, physical memory usage, and swap usage.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.stringProp("Path to the .jfr recording file")
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = getString(request.arguments(), "jfr_file_path");
                        
                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }

                        String result = analyze(filePath);
                        service.cacheResult(filePath, NAME, request.arguments(), result);
                        return CallToolResult.builder().addTextContent(result).isError(false).build();
                    } catch (Exception e) {
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
                                .build();
                    }
                })
                .build();
    }

    private String analyze(String filePath) throws IOException {
        IItemCollection events = service.loadRecording(filePath);
        StringBuilder sb = new StringBuilder();
        sb.append("# System Health Analysis\n\n");

        // CPU Load
        var cpuLoad = events.apply(ItemFilters.type("jdk.CPULoad"));
        if (cpuLoad.hasItems()) {
            sb.append("## CPU Load\n");
            double avgMachineTotal = JfrItemUtils.avgQuantity(cpuLoad, "machineTotal");
            double maxMachineTotal = JfrItemUtils.maxQuantity(cpuLoad, "machineTotal");
            double avgJvmUser = JfrItemUtils.avgQuantity(cpuLoad, "jvmUser");
            double avgJvmSystem = JfrItemUtils.avgQuantity(cpuLoad, "jvmSystem");

            sb.append(String.format("- **Avg Machine Total:** %.2f%%%n", avgMachineTotal * 100));
            sb.append(String.format("- **Max Machine Total:** %.2f%%%n", maxMachineTotal * 100));
            sb.append(String.format("- **Avg JVM User:** %.2f%%%n", avgJvmUser * 100));
            sb.append(String.format("- **Avg JVM System:** %.2f%%%n", avgJvmSystem * 100));
            sb.append("\n");
        }

        // Physical Memory
        var physicalMem = events.apply(ItemFilters.type("jdk.PhysicalMemory"));
        if (physicalMem.hasItems()) {
            sb.append("## Physical Memory\n");
            double totalSize = JfrItemUtils.maxQuantity(physicalMem, "totalSize");
            double minUsed = JfrItemUtils.minQuantity(physicalMem, "usedSize");
            double maxUsed = JfrItemUtils.maxQuantity(physicalMem, "usedSize");
            double avgUsed = JfrItemUtils.avgQuantity(physicalMem, "usedSize");

            sb.append(String.format("- **Total Physical Memory:** %s%n", formatBytes((long) totalSize)));
            sb.append(String.format("- **Min Used:** %s%n", formatBytes((long) minUsed)));
            sb.append(String.format("- **Max Used:** %s%n", formatBytes((long) maxUsed)));
            sb.append(String.format("- **Avg Used:** %s%n", formatBytes((long) avgUsed)));
            sb.append("\n");
        }

        // OS Information
        var cpuInfo = events.apply(ItemFilters.type("jdk.CPUInformation"));
        if (cpuInfo.hasItems()) {
            sb.append("## CPU Information\n");
            
            // Get first available item safely
            Optional<IItem> firstItem = cpuInfo.stream()
                .flatMap(iterable -> iterable.stream())
                .findFirst();

            if (firstItem.isPresent()) {
                IItem item = firstItem.get();
                Object cpuName = JfrItemUtils.getMember(item, "cpu");
                if (cpuName != null) sb.append(String.format("- **CPU:** %s%n", cpuName));
            }

            double cores = JfrItemUtils.maxQuantity(cpuInfo, "cores");
            double sockets = JfrItemUtils.maxQuantity(cpuInfo, "sockets");

            sb.append(String.format("- **Cores:** %.0f%n", cores));
            sb.append(String.format("- **Sockets:** %.0f%n", sockets));
            sb.append("\n");
        }

        if (!cpuLoad.hasItems() && !physicalMem.hasItems() && !cpuInfo.hasItems()) {
            sb.append("No system health events found in this recording.\n");
        }

        return sb.toString();
    }

    private static String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.2f %sB", bytes / Math.pow(1024, exp), pre);
    }

    @SuppressWarnings("unchecked")
    private static String getString(Map<String, Object> args, String key) {
        Object val = args.get(key);
        if (val == null) {
            throw new IllegalArgumentException("Missing required argument: " + key);
        }
        return val.toString();
    }
}
