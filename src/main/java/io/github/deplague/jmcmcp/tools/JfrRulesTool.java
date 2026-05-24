package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.flightrecorder.rules.IResult;
import org.openjdk.jmc.flightrecorder.rules.IRule;
import org.openjdk.jmc.flightrecorder.rules.RuleRegistry;
import org.openjdk.jmc.flightrecorder.rules.Severity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RunnableFuture;

/**
 * MCP tool for JFR rules engine.
 */
public final class JfrRulesTool {

    private static final String NAME = "jfr_rules";

    private final JfrAnalysisService service;

    public JfrRulesTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Automatically detects performance issues using JMC's built-in rules engine.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp(),
                                        "min_severity", SchemaUtil.stringProp(
                                                "Minimum rule severity threshold to include (OK, INFO, WARNING, IGNORE). Default is WARNING.",
                                                List.of("OK", "INFO", "WARNING", "IGNORE"))
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);
                        String minSevStr = SchemaUtil.getStringOrDefault(request.arguments(), "min_severity", "WARNING");

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }

                        String result = analyze(filePath, startTimeStr, endTimeStr, minSevStr);
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

    private String analyze(String filePath, String startTimeStr, String endTimeStr, String minSevStr) throws Exception {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        Severity threshold = Severity.valueOf(minSevStr);

        StringBuilder sb = new StringBuilder();
        sb.append("# JMC Automated Bottleneck Detection\n\n");
        sb.append(String.format("Rules with severity >= %s are shown.\n\n", threshold.getLocalizedName()));

        List<ResultEntry> significantResults = new ArrayList<>();
        for (IRule rule : RuleRegistry.getRules()) {
            try {
                // In JMC 9, createEvaluation takes 3 args: items, preference provider, result value provider
                RunnableFuture<IResult> future = rule.createEvaluation(events, null, null);
                future.run();
                IResult r = future.get();

                if (r.getSeverity().compareTo(threshold) >= 0) {
                    significantResults.add(new ResultEntry(rule.getName(), r.getSeverity(), r.getSummary(), r.getExplanation()));
                }
            } catch (Exception e) {
                // Skip rules that fail
            }
        }

        significantResults.sort((a, b) -> b.severity.compareTo(a.severity));

        if (significantResults.isEmpty()) {
            sb.append("No issues found exceeding the threshold.\n");
        } else {
            for (ResultEntry entry : significantResults) {
                sb.append(String.format("### %s (%s)%n", entry.name, entry.severity.getLocalizedName()));
                sb.append("**Summary:** ").append(entry.shortDesc).append("\n\n");
                if (entry.explanation != null) {
                    sb.append(entry.explanation).append("\n\n");
                }
            }
        }

        return sb.toString();
    }

    private record ResultEntry(String name, Severity severity, String shortDesc, String explanation) {
    }
}
