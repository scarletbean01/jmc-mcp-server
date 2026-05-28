package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.CorrelateApplicationService;
import io.github.deplague.jmcmcp.domain.model.CorrelateHotMethod;
import io.github.deplague.jmcmcp.domain.model.CorrelateIoSite;
import io.github.deplague.jmcmcp.domain.model.CorrelateLockSite;
import io.github.deplague.jmcmcp.domain.model.CorrelateResult;
import io.github.deplague.jmcmcp.domain.model.CpuGcMetrics;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * MCP tool adapter for cross-dimensional correlation engine.
 * Delegates analysis to the application layer and formats results as Markdown.
 */
@ApplicationScoped
public final class CorrelateTool implements McpTool {

    private static final String NAME = "smart_correlate";

    private final CorrelateApplicationService applicationService;

    @Inject
    public CorrelateTool(CorrelateApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description(
                                        "Cross-dimensional correlation engine that automatically links locks, I/O, and hot methods "
                                                + "to identify correlated request paths and bottleneck chains."
                                )
                                .inputSchema(
                                        SchemaUtil.objectSchema(
                                                SchemaUtil.props(
                                                        "jfr_file_path",
                                                        SchemaUtil.jfrFileProp(),
                                                        "dimension",
                                                        SchemaUtil.stringProp(
                                                                "Correlation dimension: lock_io_db, cpu_gc, or all (default)",
                                                                List.of("lock_io_db", "cpu_gc", "all")
                                                        ),
                                                        "start_time",
                                                        SchemaUtil.startTimeProp(),
                                                        "end_time",
                                                        SchemaUtil.endTimeProp(),
                                                        "top_n",
                                                        SchemaUtil.intProp(
                                                                "Number of top results per section (default 10)",
                                                                10
                                                        ),
                                                        "async",
                                                        SchemaUtil.boolProp(
                                                                "Run analysis asynchronously and return a job ID",
                                                                false
                                                        )
                                                ),
                                                SchemaUtil.required("jfr_file_path")
                                        )
                                )
                                .build()
                )
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(
                                request.arguments(),
                                "jfr_file_path"
                        );
                        String dimension = SchemaUtil.getStringOrDefault(
                                request.arguments(),
                                "dimension",
                                "all"
                        );
                        String startTimeStr = SchemaUtil.getStringOrDefault(
                                request.arguments(),
                                "start_time",
                                null
                        );
                        String endTimeStr = SchemaUtil.getStringOrDefault(
                                request.arguments(),
                                "end_time",
                                null
                        );
                        int topN = SchemaUtil.getIntOrDefault(
                                request.arguments(),
                                "top_n",
                                10
                        );

                        CorrelateResult result = applicationService.analyze(
                                filePath,
                                dimension,
                                startTimeStr,
                                endTimeStr,
                                topN
                        );

                        String markdown = formatMarkdown(result);
                        return CallToolResult.builder()
                                .addTextContent(markdown)
                                .isError(false)
                                .build();
                    } catch (IllegalArgumentException e) {
                        if (e.getMessage() != null && e.getMessage().startsWith("Missing required argument")) {
                            return CallToolResult.builder()
                                    .addTextContent(e.getMessage())
                                    .isError(true)
                                    .build();
                        }
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
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

    private String formatMarkdown(CorrelateResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Cross-Dimensional Correlation Analysis\n\n");

        if (result.showLockIo()) {
            appendLockIoCorrelation(sb, result.lockSites(), result.ioSites(), result.topN());
            appendHotMethodLockCorrelation(sb, result.hotMethods(), result.lockSites(), result.topN());
            appendHotMethodIoCorrelation(sb, result.hotMethods(), result.ioSites(), result.topN());
            appendBottleneckChain(sb, result.hotMethods(), result.lockSites(), result.ioSites());
        }

        if (result.showCpuGc()) {
            appendCpuGcCorrelation(sb, result.cpuGcMetrics());
        }

        sb.append("<agent_hint>Correlation analysis complete. ");
        sb.append("Use `request_waterfall` to trace specific threads, ");
        sb.append("`thread_contention` for lock details, or `io_hotspots` for I/O analysis.</agent_hint>\n");

        return sb.toString();
    }

    private void appendLockIoCorrelation(
            StringBuilder sb,
            List<CorrelateLockSite> lockSites,
            List<CorrelateIoSite> ioSites,
            int topN
    ) {
        sb.append("## Lock ↔ I/O Correlation\n\n");
        if (lockSites.isEmpty() || ioSites.isEmpty()) {
            sb.append("Insufficient lock or I/O data for correlation.\n\n");
            return;
        }

        sb.append("| Lock | I/O Under Lock | Total Lock Time | I/O Time Under Lock | % I/O Under Lock |\n");
        sb.append("|------|---------------|-----------------|---------------------|------------------|\n");

        long totalLockDuration = lockSites.stream()
                .mapToLong(CorrelateLockSite::totalDurationMs)
                .sum();
        long totalIoDuration = ioSites.stream()
                .mapToLong(CorrelateIoSite::totalDurationMs)
                .sum();

        int count = 0;
        for (CorrelateLockSite lock : lockSites) {
            if (count >= topN) {
                break;
            }
            double ioUnderLockPct = totalIoDuration > 0
                    ? ((totalIoDuration * 100.0) / (totalLockDuration + totalIoDuration))
                    : 0;
            String ioEndpoints = ioSites.stream()
                    .limit(3)
                    .map(CorrelateIoSite::endpoint)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("N/A");

            sb.append(String.format(
                    "| `%s` | %s | %s | %s | %.1f%% |\n",
                    lock.monitorClass(),
                    ioEndpoints,
                    SchemaUtil.formatDuration(lock.totalDurationMs()),
                    SchemaUtil.formatDuration(totalIoDuration),
                    ioUnderLockPct
            ));
            count++;
        }
        sb.append("\n");

        if (totalLockDuration > 0 && totalIoDuration > totalLockDuration * 0.5) {
            sb.append(
                    "**Interpretation:** Locks where >50% of hold time is I/O indicate blocking I/O under lock — a critical anti-pattern. Consider decoupling I/O from synchronized blocks.\n\n"
            );
        }
    }

    private void appendHotMethodLockCorrelation(
            StringBuilder sb,
            List<CorrelateHotMethod> hotMethods,
            List<CorrelateLockSite> lockSites,
            int topN
    ) {
        sb.append("## Hot Method ↔ Lock Correlation\n\n");
        if (hotMethods.isEmpty() || lockSites.isEmpty()) {
            sb.append("Insufficient hot method or lock data for correlation.\n\n");
            return;
        }

        sb.append("| Hot Method | Lock Contentions | Total Contention Time | % of Total Contention |\n");
        sb.append("|------------|-----------------|----------------------|----------------------|\n");

        long totalContentionMs = lockSites.stream()
                .mapToLong(CorrelateLockSite::totalDurationMs)
                .sum();

        int count = 0;
        for (CorrelateHotMethod method : hotMethods) {
            if (count >= topN) {
                break;
            }
            List<CorrelateLockSite> relatedLocks = lockSites.stream()
                    .filter(ls -> ls.topFrame().contains(method.methodName()) || method.methodName().contains(ls.topFrame()))
                    .toList();

            if (relatedLocks.isEmpty()) {
                continue;
            }

            long relatedDuration = relatedLocks.stream()
                    .mapToLong(CorrelateLockSite::totalDurationMs)
                    .sum();
            double pct = totalContentionMs > 0
                    ? ((relatedDuration * 100.0) / totalContentionMs)
                    : 0;

            sb.append(String.format(
                    "| `%s` | %s | %s | %.1f%% |\n",
                    method.methodName(),
                    relatedLocks.stream()
                            .map(ls -> "`" + ls.monitorClass() + "`")
                            .reduce((a, b) -> a + ", " + b)
                            .orElse("N/A"),
                    SchemaUtil.formatDuration(relatedDuration),
                    pct
            ));
            count++;
        }
        sb.append("\n");
    }

    private void appendHotMethodIoCorrelation(
            StringBuilder sb,
            List<CorrelateHotMethod> hotMethods,
            List<CorrelateIoSite> ioSites,
            int topN
    ) {
        sb.append("## Hot Method ↔ I/O Correlation\n\n");
        if (hotMethods.isEmpty() || ioSites.isEmpty()) {
            sb.append("Insufficient hot method or I/O data for correlation.\n\n");
            return;
        }

        sb.append("| Hot Method | I/O Endpoints | Total I/O Time | % of Total I/O |\n");
        sb.append("|------------|---------------|----------------|----------------|\n");

        long totalIoMs = ioSites.stream()
                .mapToLong(CorrelateIoSite::totalDurationMs)
                .sum();

        int count = 0;
        for (CorrelateHotMethod method : hotMethods) {
            if (count >= topN) {
                break;
            }
            List<CorrelateIoSite> relatedIo = ioSites.stream()
                    .filter(io -> io.topFrame().contains(method.methodName()) || method.methodName().contains(io.topFrame()))
                    .toList();

            if (relatedIo.isEmpty()) {
                continue;
            }

            long relatedDuration = relatedIo.stream()
                    .mapToLong(CorrelateIoSite::totalDurationMs)
                    .sum();
            double pct = totalIoMs > 0
                    ? ((relatedDuration * 100.0) / totalIoMs)
                    : 0;

            sb.append(String.format(
                    "| `%s` | %s | %s | %.1f%% |\n",
                    method.methodName(),
                    relatedIo.stream()
                            .map(io -> "`" + io.endpoint() + "`")
                            .reduce((a, b) -> a + ", " + b)
                            .orElse("N/A"),
                    SchemaUtil.formatDuration(relatedDuration),
                    pct
            ));
            count++;
        }
        sb.append("\n");
    }

    private void appendBottleneckChain(
            StringBuilder sb,
            List<CorrelateHotMethod> hotMethods,
            List<CorrelateLockSite> lockSites,
            List<CorrelateIoSite> ioSites
    ) {
        sb.append("## Bottleneck Chain\n\n");
        sb.append("The longest sequential dependency chain identified:\n\n```\n");

        int step = 1;
        for (CorrelateHotMethod method : hotMethods.stream().limit(2).toList()) {
            sb.append(step++)
                    .append(". CPU: ")
                    .append(method.methodName())
                    .append(" [hot method]\n");

            List<CorrelateLockSite> relatedLocks = lockSites.stream()
                    .filter(ls -> ls.topFrame().contains(method.methodName()) || method.methodName().contains(ls.topFrame()))
                    .limit(1)
                    .toList();
            for (CorrelateLockSite lock : relatedLocks) {
                sb.append("   ↓ (same thread)\n");
                sb.append(step++)
                        .append(". BLOCKED: ")
                        .append(lock.monitorClass())
                        .append(" [")
                        .append(SchemaUtil.formatDuration(lock.totalDurationMs() / Math.max(lock.count(), 1)))
                        .append(" avg wait]\n");

                List<CorrelateIoSite> relatedIo = ioSites.stream()
                        .filter(io -> io.topFrame().contains(method.methodName()) || method.methodName().contains(io.topFrame()))
                        .limit(2)
                        .toList();
                for (CorrelateIoSite io : relatedIo) {
                    sb.append("   ↓ (acquired, then)\n");
                    sb.append(step++)
                            .append(". I/O: ")
                            .append(io.endpoint())
                            .append(" [")
                            .append(SchemaUtil.formatDuration(io.totalDurationMs() / Math.max(io.count(), 1)))
                            .append(" avg]\n");
                }
            }
        }

        if (step == 1) {
            sb.append("No clear bottleneck chain identified from available data.\n");
        }
        sb.append("```\n\n");
    }

    private void appendCpuGcCorrelation(StringBuilder sb, CpuGcMetrics metrics) {
        sb.append("## CPU ↔ GC Correlation\n\n");

        if (metrics == null || (metrics.avgCpuLoad() == null && metrics.maxCpuLoad() == null)) {
            sb.append("No CPU load data available.\n\n");
            return;
        }

        sb.append("| Metric | Value |\n|--------|-------|\n");
        if (metrics.avgCpuLoad() != null) {
            sb.append(String.format("| Avg CPU Load | %.1f%% |\n", metrics.avgCpuLoad()));
        }
        if (metrics.maxCpuLoad() != null) {
            sb.append(String.format("| Max CPU Load | %.1f%% |\n", metrics.maxCpuLoad()));
        }
        sb.append("| GC Pause Count | ").append(metrics.gcPauseCount()).append(" |\n");
        sb.append("| Total GC Pause Time | ")
                .append(metrics.totalGcPauseTime())
                .append(" |\n\n");

        if (metrics.avgCpuLoad() != null && metrics.avgCpuLoad() > 75.0 && metrics.gcPauseCount() > 10) {
            sb.append(
                    "**Interpretation:** High CPU load combined with frequent GC pauses suggests memory pressure causing both GC overhead and CPU thrashing.\n\n"
            );
        }
    }
}
