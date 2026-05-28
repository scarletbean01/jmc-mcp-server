package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.DiffStackTracesApplicationService;
import io.github.deplague.jmcmcp.domain.model.DiffStackTracesResult;
import io.github.deplague.jmcmcp.domain.model.MethodDiffEntry;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for method-level diff between two JFR recordings.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class DiffStackTracesTool implements McpTool {

    private static final String NAME = "smart_diff_stack_traces";

    private final DiffStackTracesApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Compare hot methods between two JFR recordings at the method level. "
                                + "Shows new methods, disappeared methods, and changed prominence (>20% change).")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "baseline_jfr_path", SchemaUtil.stringProp("Path to baseline JFR recording"),
                                        "target_jfr_path", SchemaUtil.stringProp("Path to target JFR recording"),
                                        "package_prefix", SchemaUtil.stringProp("Optional package prefix to filter (e.g., 'com.mycompany')"),
                                        "top_n", SchemaUtil.intProp("Number of top methods per category (default 20)", 20),
                                        "async", SchemaUtil.boolProp("Run analysis asynchronously and return a job ID", false)
                                ),
                                SchemaUtil.required("baseline_jfr_path", "target_jfr_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String baselinePath = SchemaUtil.getString(request.arguments(), "baseline_jfr_path");
                        String targetPath = SchemaUtil.getString(request.arguments(), "target_jfr_path");
                        String packagePrefix = SchemaUtil.getStringOrDefault(request.arguments(), "package_prefix", null);
                        int topN = SchemaUtil.getIntOrDefault(request.arguments(), "top_n", 20);

                        DiffStackTracesResult result = appService.analyze(
                                baselinePath, targetPath, packagePrefix, topN);
                        String markdown = formatMarkdown(result, topN);
                        return CallToolResult.builder()
                                .addTextContent(markdown)
                                .isError(false)
                                .build();
                    } catch (Exception e) {
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
                                .build();
                    }
                })
                .build();
    }

    public String formatMarkdown(DiffStackTracesResult result, int topN) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Stack Trace Diff\n\n");

        sb.append("## Recording Context\n\n");
        sb.append("| Recording | Duration | Total Samples | Samples/sec |\n");
        sb.append("|-----------|----------|---------------|-------------|\n");
        sb.append(String.format("| Baseline | %.1fs | %d | %.1f/s |\n",
                result.baselineDurationSec(), result.baselineTotalSamples(),
                result.baselineTotalSamples() / result.baselineDurationSec()));
        sb.append(String.format("| Target | %.1fs | %d | %.1f/s |\n\n",
                result.targetDurationSec(), result.targetTotalSamples(),
                result.targetTotalSamples() / result.targetDurationSec()));

        sb.append("## New Methods (in target, not in baseline)\n\n");
        if (result.newMethods().isEmpty()) {
            sb.append("No new methods found.\n\n");
        } else {
            sb.append("| Method | Target Samples/s | Baseline Samples/s | Change |\n");
            sb.append("|--------|-----------------|-------------------|--------|\n");
            result.newMethods().stream().limit(topN)
                    .forEach(d -> sb.append(String.format("| `%s` | %.1f | 0 | NEW |\n",
                            d.methodName(), d.targetRate())));
            sb.append("\n");
        }

        sb.append("## Disappeared Methods (in baseline, not in target)\n\n");
        if (result.disappearedMethods().isEmpty()) {
            sb.append("No disappeared methods found.\n\n");
        } else {
            sb.append("| Method | Baseline Samples/s | Target Samples/s | Change |\n");
            sb.append("|--------|-------------------|-----------------|--------|\n");
            result.disappearedMethods().stream().limit(topN)
                    .forEach(d -> sb.append(String.format("| `%s` | %.1f | 0 | REMOVED |\n",
                            d.methodName(), d.baselineRate())));
            sb.append("\n");
        }

        sb.append("## Changed Prominence (>20% change, normalized)\n\n");
        if (result.changedMethods().isEmpty()) {
            sb.append("No significantly changed methods found.\n\n");
        } else {
            sb.append("| Method | Baseline Samples/s | Target Samples/s | Change | % Change |\n");
            sb.append("|--------|-------------------|-----------------|--------|----------|\n");
            result.changedMethods().stream().limit(topN)
                    .forEach(d -> sb.append(String.format("| `%s` | %.1f | %.1f | %.1f | %.1f%% |\n",
                            d.methodName(), d.baselineRate(), d.targetRate(),
                            d.absoluteChange(), d.pctChange())));
            sb.append("\n");
        }

        sb.append("## Stable Methods (<20% change, top 10)\n\n");
        if (result.stableMethods().isEmpty()) {
            sb.append("No stable methods found.\n\n");
        } else {
            sb.append("| Method | Baseline Samples/s | Target Samples/s | % Change |\n");
            sb.append("|--------|-------------------|-----------------|----------|\n");
            result.stableMethods().stream().limit(10)
                    .forEach(d -> sb.append(String.format("| `%s` | %.1f | %.1f | %.1f%% |\n",
                            d.methodName(), d.baselineRate(), d.targetRate(), d.pctChange())));
            sb.append("\n");
        }

        sb.append("<agent_hint>Significant changes detected between recordings. Consider `compare_recordings` for metric-level comparison or `correlate` for deeper analysis of the target recording.</agent_hint>\n");

        return sb.toString();
    }
}
