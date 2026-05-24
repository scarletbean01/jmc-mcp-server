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
import java.util.ArrayList;
import java.util.List;

import static io.github.deplague.jmcmcp.tools.SchemaUtil.*;

/**
 * MCP tool for object statistics and heap occupancy analysis.
 */
public final class ObjectStatisticsTool {

    private static final String NAME = "object_statistics";

    private final JfrAnalysisService service;

    public ObjectStatisticsTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze object statistics and heap occupancy in a JFR recording. " +
                                "Identifies classes with highest instance counts and total size.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top classes to return (default 10)", 10)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = getStringOrDefault(request.arguments(), "end_time", null);
                        int topN = getIntOrDefault(request.arguments(), "top_n", 10);

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
        sb.append("# Object Statistics Analysis\n\n");

        IItemCollection statsEvents = events.apply(ItemFilters.type("jdk.ObjectCount"));
        if (statsEvents.hasItems()) {
            sb.append("## Heap Occupancy (Live Objects)\n");
            sb.append("| Class | Count | Total Size |\n");
            sb.append("|-------|-------|------------|\n");

            List<IItem> sortedStats = new ArrayList<>();
            for (IItemIterable iterable : statsEvents) {
                for (IItem item : iterable) {
                    sortedStats.add(item);
                }
            }

            sortedStats.stream()
                    .sorted((a, b) -> {
                        IQuantity sa = JfrItemUtils.getQuantity(a, "totalSize").orElse(null);
                        IQuantity sb1 = JfrItemUtils.getQuantity(b, "totalSize").orElse(null);
                        if (sa == null) return (sb1 == null) ? 0 : 1;
                        if (sb1 == null) return -1;
                        return sb1.compareTo(sa);
                    })
                    .limit(topN)
                    .forEach(item -> {
                        Object clazz = JfrItemUtils.getMember(item, "objectClass").orElse(null);
                        IQuantity count = JfrItemUtils.getQuantity(item, "count").orElse(null);
                        IQuantity size = JfrItemUtils.getQuantity(item, "totalSize").orElse(null);
                        sb.append(String.format("| `%s` | %s | %s |%n", clazz, JfrAnalysisService.display(count), JfrAnalysisService.display(size)));
                    });
        } else {
            sb.append("No object count events found. Make sure -XX:StartFlightRecording:settings=profile is used or the 'Object Count' event is enabled.\n");
        }

        return sb.toString();
    }

}
