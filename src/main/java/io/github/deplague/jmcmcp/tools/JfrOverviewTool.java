package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrAnalysisService.RecordingOverview;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.ItemFilters;
import org.openjdk.jmc.common.item.IItemCollection;

import java.io.IOException;
import java.util.Map;

/**
 * MCP tool that returns a high-level overview of a JFR recording.
 */
public final class JfrOverviewTool {

    private static final String NAME = "jfr_overview";

    private final JfrAnalysisService service;

    public JfrOverviewTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Return a high-level summary of a JFR recording: " +
                                "duration, event type counts, JVM information, and available analysis categories.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.stringProp("Absolute or relative path to the .jfr recording file")
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = getString(request.arguments(), "jfr_file_path");
                        String result = analyze(filePath);
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
        RecordingOverview overview = service.getOverview(filePath);
        IItemCollection events = service.loadRecording(filePath);

        StringBuilder sb = new StringBuilder();
        sb.append("# JFR Recording Overview\n\n");
        sb.append("**File:** ").append(overview.filePath()).append("\n");
        sb.append("**Duration:** ").append(String.format("%.2f", overview.durationSeconds())).append(" seconds\n\n");

        // JVM info if available
        var jvmInfo = events.apply(ItemFilters.type("jdk.JVMInformation"));
        if (jvmInfo.hasItems()) {
            sb.append("## JVM Information\n");
            for (var item : jvmInfo) {
                for (var i : item) {
                    Object jvmVersion = io.github.deplague.jmcmcp.jfr.JfrItemUtils.getMember(i, "jvmVersion");
                    Object jvmName = io.github.deplague.jmcmcp.jfr.JfrItemUtils.getMember(i, "jvmName");
                    Object jvmArgs = io.github.deplague.jmcmcp.jfr.JfrItemUtils.getMember(i, "jvmArguments");
                    if (jvmVersion != null) sb.append("- **JVM Version:** ").append(jvmVersion).append("\n");
                    if (jvmName != null) sb.append("- **JVM Name:** ").append(jvmName).append("\n");
                    if (jvmArgs != null) sb.append("- **JVM Arguments:** ").append(jvmArgs).append("\n");
                    break;
                }
                break;
            }
            sb.append("\n");
        }

        // Event counts
        sb.append("## Event Type Counts\n");
        overview.eventCounts().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(30)
                .forEach(e -> sb.append(String.format("- `%s`: %d%n", e.getKey(), e.getValue())));

        if (overview.eventCounts().size() > 30) {
            sb.append("- ... and ").append(overview.eventCounts().size() - 30).append(" more event types\n");
        }

        return sb.toString();
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
