package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.Aggregators;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.flightrecorder.JfrAttributes;

import java.io.IOException;
import java.util.List;

/**
 * MCP tool for analyzing I/O events (File and Socket).
 */
public final class IoAnalysisTool {

    private static final String NAME = "io_analysis";

    private final JfrAnalysisService service;

    public IoAnalysisTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze file and socket I/O events in a JFR recording. " +
                                "Reports read/write durations and throughput.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "io_type", SchemaUtil.stringProp(
                                                "Which I/O events to analyze: file, socket, or all (default)",
                                                List.of("file", "socket", "all"))
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        String ioType = SchemaUtil.getStringOrDefault(request.arguments(), "io_type", "all");

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }

                        String result = analyze(filePath, startTimeStr, endTimeStr, ioType);
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

    private String analyze(String filePath, String startTimeStr, String endTimeStr, String ioType) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);
        StringBuilder sb = new StringBuilder();
        sb.append("# I/O Analysis\n\n");

        boolean any = false;

        // File I/O
        if ("all".equals(ioType) || "file".equals(ioType)) {
            var fileEvents = events.apply(ItemFilters.type("jdk.FileRead", "jdk.FileWrite"));
            if (fileEvents.hasItems()) {
                any = true;
                sb.append("## File I/O\n");
                sb.append(String.format("- **Event Count:** %s%n", JfrAnalysisService.display(fileEvents.getAggregate(Aggregators.count()))));
                sb.append(String.format("- **Total Duration:** %s%n", JfrAnalysisService.display(fileEvents.getAggregate(Aggregators.sum(JfrAttributes.DURATION)))));
                sb.append(String.format("- **Average Duration:** %s%n", JfrAnalysisService.display(fileEvents.getAggregate(Aggregators.avg(JfrAttributes.DURATION)))));

                IQuantity totalRead = JfrItemUtils.sumQuantity(events.apply(ItemFilters.type("jdk.FileRead")), "bytesRead");
                IQuantity totalWrite = JfrItemUtils.sumQuantity(events.apply(ItemFilters.type("jdk.FileWrite")), "bytesWritten");
                sb.append(String.format("- **Total Read:** %s%n", JfrAnalysisService.display(totalRead)));
                sb.append(String.format("- **Total Written:** %s%n", JfrAnalysisService.display(totalWrite)));
                sb.append("\n");
            }
        }

        // Socket I/O
        if ("all".equals(ioType) || "socket".equals(ioType)) {
            var socketEvents = events.apply(ItemFilters.type("jdk.SocketRead", "jdk.SocketWrite"));
            if (socketEvents.hasItems()) {
                any = true;
                sb.append("## Socket I/O\n");
                sb.append(String.format("- **Event Count:** %s%n", JfrAnalysisService.display(socketEvents.getAggregate(Aggregators.count()))));
                sb.append(String.format("- **Total Duration:** %s%n", JfrAnalysisService.display(socketEvents.getAggregate(Aggregators.sum(JfrAttributes.DURATION)))));
                sb.append(String.format("- **Average Duration:** %s%n", JfrAnalysisService.display(socketEvents.getAggregate(Aggregators.avg(JfrAttributes.DURATION)))));

                IQuantity totalRead = JfrItemUtils.sumQuantity(events.apply(ItemFilters.type("jdk.SocketRead")), "bytesRead");
                IQuantity totalWrite = JfrItemUtils.sumQuantity(events.apply(ItemFilters.type("jdk.SocketWrite")), "bytesWritten");
                sb.append(String.format("- **Total Read:** %s%n", JfrAnalysisService.display(totalRead)));
                sb.append(String.format("- **Total Written:** %s%n", JfrAnalysisService.display(totalWrite)));
                sb.append("\n");
            }
        }

        if (!any) {
            sb.append("No I/O events found in this recording range.\n");
        }

        return sb.toString();
    }


}
