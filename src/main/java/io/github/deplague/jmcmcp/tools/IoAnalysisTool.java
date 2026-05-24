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
import java.util.Map;

/**
 * MCP tool for File and Socket I/O analysis.
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
                                "Reports read/write counts, total bytes transferred, and latency statistics.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.stringProp("Path to the .jfr recording file"),
                                        "io_type", SchemaUtil.stringProp(
                                                "Which I/O events to analyze: file, socket, or all (default)",
                                                List.of("file", "socket", "all"))
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = getString(request.arguments(), "jfr_file_path");
                        String ioType = getStringOrDefault(request.arguments(), "io_type", "all");
                        String result = analyze(filePath, ioType);
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

    private String analyze(String filePath, String ioType) throws IOException {
        IItemCollection events = service.loadRecording(filePath);
        StringBuilder sb = new StringBuilder();
        sb.append("# I/O Analysis\n\n");

        boolean any = false;

        if ("all".equals(ioType) || "file".equals(ioType)) {
            any |= analyzeFileIO(events, sb);
        }
        if ("all".equals(ioType) || "socket".equals(ioType)) {
            any |= analyzeSocketIO(events, sb);
        }

        if (!any) {
            sb.append("No I/O events found in this recording.\n");
        }

        return sb.toString();
    }

    private boolean analyzeFileIO(IItemCollection events, StringBuilder sb) {
        var fileRead = events.apply(ItemFilters.type("jdk.FileRead"));
        var fileWrite = events.apply(ItemFilters.type("jdk.FileWrite"));
        boolean hasRead = fileRead.hasItems();
        boolean hasWrite = fileWrite.hasItems();

        if (hasRead || hasWrite) {
            sb.append("## File I/O\n\n");
        }

        if (hasRead) {
            sb.append("### File Read\n");
            IQuantity count = fileRead.getAggregate(Aggregators.count());
            double totalBytes = JfrItemUtils.sumQuantity(fileRead, "bytesRead");
            IQuantity avgDuration = fileRead.getAggregate(Aggregators.avg(JfrAttributes.DURATION));
            IQuantity maxDuration = fileRead.getAggregate(Aggregators.max(JfrAttributes.DURATION));

            sb.append(String.format("- **Count:** %s%n", JfrAnalysisService.display(count)));
            sb.append(String.format("- **Total Bytes Read:** %.2f%n", totalBytes));
            sb.append(String.format("- **Average Duration:** %s%n", JfrAnalysisService.display(avgDuration)));
            sb.append(String.format("- **Max Duration:** %s%n", JfrAnalysisService.display(maxDuration)));
            sb.append("\n");
        }

        if (hasWrite) {
            sb.append("### File Write\n");
            IQuantity count = fileWrite.getAggregate(Aggregators.count());
            double totalBytes = JfrItemUtils.sumQuantity(fileWrite, "bytesWritten");
            IQuantity avgDuration = fileWrite.getAggregate(Aggregators.avg(JfrAttributes.DURATION));
            IQuantity maxDuration = fileWrite.getAggregate(Aggregators.max(JfrAttributes.DURATION));

            sb.append(String.format("- **Count:** %s%n", JfrAnalysisService.display(count)));
            sb.append(String.format("- **Total Bytes Written:** %.2f%n", totalBytes));
            sb.append(String.format("- **Average Duration:** %s%n", JfrAnalysisService.display(avgDuration)));
            sb.append(String.format("- **Max Duration:** %s%n", JfrAnalysisService.display(maxDuration)));
            sb.append("\n");
        }

        return hasRead || hasWrite;
    }

    private boolean analyzeSocketIO(IItemCollection events, StringBuilder sb) {
        var socketRead = events.apply(ItemFilters.type("jdk.SocketRead"));
        var socketWrite = events.apply(ItemFilters.type("jdk.SocketWrite"));
        boolean hasRead = socketRead.hasItems();
        boolean hasWrite = socketWrite.hasItems();

        if (hasRead || hasWrite) {
            sb.append("## Socket I/O\n\n");
        }

        if (hasRead) {
            sb.append("### Socket Read\n");
            IQuantity count = socketRead.getAggregate(Aggregators.count());
            double totalBytes = JfrItemUtils.sumQuantity(socketRead, "bytesRead");
            IQuantity avgDuration = socketRead.getAggregate(Aggregators.avg(JfrAttributes.DURATION));
            IQuantity maxDuration = socketRead.getAggregate(Aggregators.max(JfrAttributes.DURATION));

            sb.append(String.format("- **Count:** %s%n", JfrAnalysisService.display(count)));
            sb.append(String.format("- **Total Bytes Read:** %.2f%n", totalBytes));
            sb.append(String.format("- **Average Duration:** %s%n", JfrAnalysisService.display(avgDuration)));
            sb.append(String.format("- **Max Duration:** %s%n", JfrAnalysisService.display(maxDuration)));
            sb.append("\n");
        }

        if (hasWrite) {
            sb.append("### Socket Write\n");
            IQuantity count = socketWrite.getAggregate(Aggregators.count());
            double totalBytes = JfrItemUtils.sumQuantity(socketWrite, "bytesWritten");
            IQuantity avgDuration = socketWrite.getAggregate(Aggregators.avg(JfrAttributes.DURATION));
            IQuantity maxDuration = socketWrite.getAggregate(Aggregators.max(JfrAttributes.DURATION));

            sb.append(String.format("- **Count:** %s%n", JfrAnalysisService.display(count)));
            sb.append(String.format("- **Total Bytes Written:** %.2f%n", totalBytes));
            sb.append(String.format("- **Average Duration:** %s%n", JfrAnalysisService.display(avgDuration)));
            sb.append(String.format("- **Max Duration:** %s%n", JfrAnalysisService.display(maxDuration)));
            sb.append("\n");
        }

        return hasRead || hasWrite;
    }

    @SuppressWarnings("unchecked")
    private static String getString(Map<String, Object> args, String key) {
        Object val = args.get(key);
        if (val == null) {
            throw new IllegalArgumentException("Missing required argument: " + key);
        }
        return val.toString();
    }

    private static String getStringOrDefault(Map<String, Object> args, String key, String defaultValue) {
        Object val = args.get(key);
        return val != null ? val.toString() : defaultValue;
    }
}
