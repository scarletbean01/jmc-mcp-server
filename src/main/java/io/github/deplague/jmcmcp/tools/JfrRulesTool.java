package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.util.IPreferenceValueProvider;
import org.openjdk.jmc.flightrecorder.rules.IRule;
import org.openjdk.jmc.flightrecorder.rules.IResult;
import org.openjdk.jmc.flightrecorder.rules.RuleRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * MCP tool that runs all built-in JMC analysis rules to automatically detect bottlenecks.
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
                        .description("Run all built-in JMC (Java Mission Control) analysis rules on a JFR recording. " +
                                "Automatically detects performance bottlenecks such as GC issues, hot methods, " +
                                "thread contention, memory leaks, and I/O problems. Each rule produces a severity " +
                                "level (OK, INFO, WARNING, CRITICAL).")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.stringProp("Path to the .jfr recording file"),
                                        "min_score", SchemaUtil.numberProp(
                                                "Minimum rule severity threshold to include (0-100, default 50)", 50)
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = getString(request.arguments(), "jfr_file_path");
                        double minScore = getDoubleOrDefault(request.arguments(), "min_score", 50);
                        String result = analyze(filePath, minScore);
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

    private String analyze(String filePath, double minScore) throws Exception {
        IItemCollection events = service.loadRecording(filePath);
        StringBuilder sb = new StringBuilder();
        sb.append("# JMC Automated Bottleneck Detection\n\n");
        sb.append(String.format("Rules with score >= %.0f are shown.\n\n", minScore));

        List<ResultEntry> significantResults = new ArrayList<>();

        for (IRule rule : RuleRegistry.getRules()) {
            try {
                var future = rule.createEvaluation(events, IPreferenceValueProvider.DEFAULT_VALUES, null);
                future.run();
                IResult result = future.get();
                if (result != null) {
                    double score = severityToScore(result.getSeverity());
                    if (score >= minScore) {
                        significantResults.add(new ResultEntry(score, rule.getName(), result));
                    }
                }
            } catch (Exception e) {
                // Skip rules that fail to evaluate (e.g., missing events)
            }
        }

        if (significantResults.isEmpty()) {
            sb.append("No significant issues detected with the current threshold.\n");
            return sb.toString();
        }

        // Sort by score descending
        significantResults.sort((a, b) -> Double.compare(b.score, a.score));

        sb.append("| Score | Rule | Summary |\n");
        sb.append("|-------|------|---------|\n");

        for (ResultEntry entry : significantResults) {
            String summary = entry.result.getSummary() != null ? entry.result.getSummary() : "No description";
            if (summary.length() > 120) {
                summary = summary.substring(0, 117) + "...";
            }
            sb.append(String.format("| %.0f | %s | %s |%n",
                    entry.score, entry.ruleName, summary.replace("|", "\\|")));
        }

        sb.append("\n## Detailed Results\n\n");
        for (ResultEntry entry : significantResults) {
            sb.append(String.format("### %s (Score: %.0f)%n", entry.ruleName, entry.score));
            if (entry.result.getSummary() != null) {
                sb.append(String.format("- **Summary:** %s%n", entry.result.getSummary()));
            }
            if (entry.result.getExplanation() != null) {
                sb.append(String.format("- **Explanation:** %s%n", entry.result.getExplanation()));
            }
            if (entry.result.getSolution() != null) {
                sb.append(String.format("- **Solution:** %s%n", entry.result.getSolution()));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private static double severityToScore(org.openjdk.jmc.flightrecorder.rules.Severity severity) {
        if (severity == null) {
            return 0;
        }
        return switch (severity) {
            case OK -> 0;
            case INFO -> 25;
            case WARNING -> 75;
            case IGNORE, NA -> 0;
        };
    }

    private record ResultEntry(double score, String ruleName, IResult result) {
    }

    @SuppressWarnings("unchecked")
    private static String getString(Map<String, Object> args, String key) {
        Object val = args.get(key);
        if (val == null) {
            throw new IllegalArgumentException("Missing required argument: " + key);
        }
        return val.toString();
    }

    private static double getDoubleOrDefault(Map<String, Object> args, String key, double defaultValue) {
        Object val = args.get(key);
        if (val instanceof Number n) {
            return n.doubleValue();
        }
        if (val instanceof String s) {
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
}
