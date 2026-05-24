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

import java.util.Optional;

public final class ContainerMetricsTool {

    private static final String NAME = "container_metrics";
    private final JfrAnalysisService service;

    public ContainerMetricsTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze container resource limits and usage (Docker/Kubernetes).")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp()
                                ),
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

    private String analyze(String filePath, String startTimeStr, String endTimeStr) throws Exception {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        StringBuilder sb = new StringBuilder();
        sb.append("# Container Metrics Analysis\n\n");

        IItemCollection config = events.apply(ItemFilters.type("jdk.ContainerConfiguration"));
        IItemCollection cpuUsage = events.apply(ItemFilters.type("jdk.ContainerCPUUsage"));
        IItemCollection memUsage = events.apply(ItemFilters.type("jdk.ContainerMemoryUsage"));

        if (!config.hasItems() && !cpuUsage.hasItems() && !memUsage.hasItems()) {
            return sb.append("No container configuration or usage events found. The JVM is likely not running in a container, or container tracking is disabled.").toString();
        }

        if (config.hasItems()) {
            sb.append("## Container Configuration\n");
            Optional<IItem> itemOpt = config.stream().flatMap(IItemIterable::stream).findFirst();
            itemOpt.ifPresent(item -> {
                sb.append("- **CPU Shares:** ").append(JfrItemUtils.getMember(item, "cpuShares").orElse("N/A")).append("\n");
                sb.append("- **CPU Period:** ").append(JfrAnalysisService.display(JfrItemUtils.getQuantity(item, "cpuPeriod").orElse(null))).append("\n");
                sb.append("- **CPU Quota:** ").append(JfrAnalysisService.display(JfrItemUtils.getQuantity(item, "cpuQuota").orElse(null))).append("\n");
                sb.append("- **Memory Limit:** ").append(JfrAnalysisService.display(JfrItemUtils.getQuantity(item, "memoryLimit").orElse(null))).append("\n");
                sb.append("- **Swap Limit:** ").append(JfrAnalysisService.display(JfrItemUtils.getQuantity(item, "swapLimit").orElse(null))).append("\n");
                sb.append("- **Memory Soft Limit:** ").append(JfrAnalysisService.display(JfrItemUtils.getQuantity(item, "memorySoftLimit").orElse(null))).append("\n");
            });
            sb.append("\n");
        }

        if (cpuUsage.hasItems()) {
            sb.append("## Container CPU Usage\n");
            sb.append("- **Avg CPU Time:** ").append(JfrAnalysisService.display(JfrItemUtils.avgQuantity(cpuUsage, "cpuTime"))).append("\n");
            sb.append("- **Max CPU Time:** ").append(JfrAnalysisService.display(JfrItemUtils.maxQuantity(cpuUsage, "cpuTime"))).append("\n\n");
        }

        if (memUsage.hasItems()) {
            sb.append("## Container Memory Usage\n");
            sb.append("- **Avg Memory Usage:** ").append(JfrAnalysisService.display(JfrItemUtils.avgQuantity(memUsage, "memoryUsage"))).append("\n");
            sb.append("- **Max Memory Usage:** ").append(JfrAnalysisService.display(JfrItemUtils.maxQuantity(memUsage, "memoryUsage"))).append("\n");
            sb.append("- **Avg Swap Usage:** ").append(JfrAnalysisService.display(JfrItemUtils.avgQuantity(memUsage, "swapUsage"))).append("\n");
            sb.append("- **Max Swap Usage:** ").append(JfrAnalysisService.display(JfrItemUtils.maxQuantity(memUsage, "swapUsage"))).append("\n\n");
        }

        return sb.toString();
    }
}