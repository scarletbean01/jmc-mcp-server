package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * MCP tool for analyzing Java exceptions thrown during a recording.
 */
public final class ExceptionAnalysisTool {

    private static final String NAME = "exception_analysis";

    private final JfrAnalysisService service;

    public ExceptionAnalysisTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze Java exceptions in a JFR recording. " +
                                "Reports top exceptions by class, message, and throw site.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top exceptions to return (default 10)", 10)
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

        IItemCollection exceptionEvents = events.apply(ItemFilters.type("jdk.JavaExceptionThrow"));
        if (!exceptionEvents.hasItems()) {
            return "# Exception Analysis\n\nNo exception throw events found in the recording.";
        }

        Map<ExceptionKey, Long> counts = new HashMap<>();
        for (IItemIterable iterable : exceptionEvents) {
            IMemberAccessor<Object, IItem> classAccessor = JfrItemUtils.getAccessor(iterable.getType(), "thrownClass");
            IMemberAccessor<Object, IItem> msgAccessor = JfrItemUtils.getAccessor(iterable.getType(), "message");
            IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");

            if (classAccessor != null) {
                for (IItem item : iterable) {
                    String className = classAccessor.getMember(item).toString();
                    String message = msgAccessor != null ? String.valueOf(msgAccessor.getMember(item)) : "";
                    String trace = stackAccessor != null ? JfrItemUtils.formatStackTrace(stackAccessor.getMember(item), 5) : "No trace";

                    ExceptionKey key = new ExceptionKey(className, message, trace);
                    counts.merge(key, 1L, Long::sum);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Exception Analysis\n\n");
        sb.append("| Count | Exception Class | Message | Throw Site (top 5 frames) |\n");
        sb.append("|-------|-----------------|---------|---------------------------|\n");

        counts.entrySet().stream()
                .sorted(Map.Entry.<ExceptionKey, Long>comparingByValue().reversed())
                .limit(topN)
                .forEach(entry -> {
                    sb.append("| ").append(entry.getValue()).append(" | ");
                    sb.append("`").append(entry.getKey().className).append("` | ");
                    sb.append("`").append(entry.getKey().message.replace("\n", " ")).append("` | ");
                    sb.append("`").append(entry.getKey().stackTrace.replace("\n", "`<br>`")).append("` |\n");
                });

        return sb.toString();
    }

    private record ExceptionKey(String className, String message, String stackTrace) {
    }
}
