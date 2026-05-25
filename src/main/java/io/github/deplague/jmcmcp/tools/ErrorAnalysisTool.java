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

public final class ErrorAnalysisTool {

    private static final String NAME = "error_analysis";

    private final JfrAnalysisService service;

    public ErrorAnalysisTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Analyze Java errors (OutOfMemoryError, StackOverflowError, InternalError, etc.) in a JFR recording. " +
                                "Reports top errors by class, message, and throw site with severity classification.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "top_n", SchemaUtil.intProp("Number of top errors to return (default 10)", 10)
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

    String analyze(String filePath, String startTimeStr, String endTimeStr, int topN) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        IItemCollection errorEvents = events.apply(ItemFilters.type("jdk.JavaErrorThrow"));
        IItemCollection exceptionEvents = events.apply(ItemFilters.type("jdk.JavaExceptionThrow"));

        long errorCount = JfrItemUtils.count(errorEvents);
        long exceptionCount = JfrItemUtils.count(exceptionEvents);

        if (errorCount == 0) {
            return "# Error Analysis\n\nNo Java error throw events found in the recording.";
        }

        Map<ErrorKey, ErrorStats> counts = new HashMap<>();
        for (IItemIterable iterable : errorEvents) {
            IMemberAccessor<Object, IItem> classAccessor = JfrItemUtils.getAccessor(iterable.getType(), "thrownClass");
            IMemberAccessor<Object, IItem> msgAccessor = JfrItemUtils.getAccessor(iterable.getType(), "message");
            IMemberAccessor<Object, IItem> stackAccessor = JfrItemUtils.getAccessor(iterable.getType(), "stackTrace");

            if (classAccessor != null) {
                for (IItem item : iterable) {
                    String className = classAccessor.getMember(item).toString();
                    String message = msgAccessor != null ? String.valueOf(msgAccessor.getMember(item)) : "";
                    String trace = stackAccessor != null ? JfrItemUtils.formatStackTrace(stackAccessor.getMember(item), 5) : "No trace";

                    ErrorKey key = new ErrorKey(className, message, trace);
                    counts.computeIfAbsent(key, k -> new ErrorStats()).count++;
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Error Analysis\n\n");

        sb.append("## Error Summary\n\n");
        sb.append("| Metric | Value |\n");
        sb.append("|--------|-------|\n");
        sb.append("| Total Errors | ").append(errorCount).append(" |\n");
        sb.append("| Total Exceptions | ").append(exceptionCount).append(" |\n");
        if (exceptionCount > 0) {
            sb.append(String.format("| Error-to-Exception Ratio | %.2f%% |\n", (errorCount * 100.0) / exceptionCount));
        }
        sb.append("\n");

        sb.append("## Top Errors by Class\n\n");
        sb.append("| Count | Error Class | Message | Throw Site (top 5 frames) | Severity |\n");
        sb.append("|------|-------------|---------|---------------------------|----------|\n");

        counts.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().count, a.getValue().count))
                .limit(topN)
                .forEach(entry -> {
                    ErrorKey k = entry.getKey();
                    ErrorStats s = entry.getValue();
                    sb.append("| ").append(s.count).append(" | ");
                    sb.append("`").append(k.className).append("` | ");
                    sb.append("`").append(k.message.replace("\n", " ")).append("` | ");
                    sb.append("`").append(k.stackTrace.replace("\n", "`<br>`")).append("` | ");
                    sb.append(classifySeverity(k.className)).append(" |\n");
                });

        return sb.toString();
    }

    private static String classifySeverity(String className) {
        if (className.contains("OutOfMemoryError")) return "CRITICAL";
        if (className.contains("StackOverflowError")) return "HIGH";
        if (className.contains("InternalError") || className.contains("UnknownError")) return "HIGH";
        if (className.contains("ThreadDeath")) return "MEDIUM";
        return "MEDIUM";
    }

    private record ErrorKey(String className, String message, String stackTrace) {}
    private static class ErrorStats {
        long count;
    }
}