package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * MCP tool for analyzing VM operations (Safepoints, VM Stops).
 */
public final class VMOperationsTool {

    private static final String NAME = "vm_operations";

    private final JfrAnalysisService service;

    public VMOperationsTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze VM operations and safepoint events in a JFR recording. " +
                                "Reports longest VM operations and total STW time.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top VM operations to return (default 10)", 10)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        int topN = SchemaUtil.getIntOrDefault(request.arguments(), "top_n", 10);

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }

                        String result = analyze(filePath, startTimeStr, endTimeStr, topN);
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

    private String analyze(String filePath, String startTimeStr, String endTimeStr, int topN) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        StringBuilder sb = new StringBuilder();
        sb.append("# VM Operations Analysis\n\n");

        IItemCollection vmOps = events.apply(ItemFilters.type("jdk.ExecuteVMOperation"));
        if (vmOps.hasItems()) {
            IQuantity totalDuration = JfrItemUtils.sumQuantity(vmOps, JfrAttributes.DURATION.getIdentifier());
            IQuantity maxDuration = JfrItemUtils.maxQuantity(vmOps, JfrAttributes.DURATION.getIdentifier());
            IQuantity avgDuration = JfrItemUtils.avgQuantity(vmOps, JfrAttributes.DURATION.getIdentifier());

            sb.append("## Summary\n");
            sb.append(String.format("- **Total VM Ops Duration:** %s%n", JfrAnalysisService.display(totalDuration)));
            sb.append(String.format("- **Max VM Op Duration:** %s%n", JfrAnalysisService.display(maxDuration)));
            sb.append(String.format("- **Avg VM Op Duration:** %s%n", JfrAnalysisService.display(avgDuration)));
            sb.append("\n");

            sb.append("## Longest VM Operations\n");
            sb.append("| Operation | Duration | Caller |\n");
            sb.append("|-----------|----------|--------|\n");

            List<IItem> sorted = new ArrayList<>();
            vmOps.forEach(iterable -> iterable.forEach(sorted::add));
            sorted.stream()
                    .sorted((a, b) -> {
                        IQuantity da = JfrItemUtils.getQuantity(a, JfrAttributes.DURATION.getIdentifier()).orElse(null);
                        IQuantity db = JfrItemUtils.getQuantity(b, JfrAttributes.DURATION.getIdentifier()).orElse(null);
                        if (da == null) return (db == null) ? 0 : 1;
                        if (db == null) return -1;
                        return db.compareTo(da);
                    })
                    .limit(topN)
                    .forEach(item -> {
                        Object operation = JfrItemUtils.getMember(item, "operation").orElse(null);
                        IQuantity duration = JfrItemUtils.getQuantity(item, JfrAttributes.DURATION.getIdentifier()).orElse(null);
                        Object caller = JfrItemUtils.getMember(item, "caller").orElse(null);
                        sb.append(String.format("| %s | %s | %s |%n", operation, JfrAnalysisService.display(duration), caller));
                    });
        } else {
            sb.append("No VM operation events found in the recording.\n");
        }

        return sb.toString();
    }
}
