package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.application.service.JdkBugReferenceApplicationService;
import io.github.deplague.jmcmcp.domain.model.JdkBugReferenceResult;
import io.github.deplague.jmcmcp.tools.SchemaUtil;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * MCP tool adapter for JDK bug cross-reference analysis.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@ApplicationScoped
public final class JdkBugReferenceTool implements McpTool {

    private static final String NAME = "jdk_bug_reference";

    private final JdkBugReferenceApplicationService appService;

    @Override
    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder()
                                .name(NAME)
                                .description(
                                        "Cross-reference JFR events against known JDK bug signatures. "
                                                + "Matches CompilerFailure, InternalError, and other events to known OpenJDK bugs "
                                                + "and provides version-specific workarounds."
                                )
                                .inputSchema(
                                        SchemaUtil.objectSchema(
                                                SchemaUtil.props(
                                                        "jfr_file_path",
                                                        SchemaUtil.jfrFileProp(),
                                                        "start_time",
                                                        SchemaUtil.startTimeProp(),
                                                        "end_time",
                                                        SchemaUtil.endTimeProp()
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

                        JdkBugReferenceResult result = appService.analyze(
                                filePath,
                                startTimeStr,
                                endTimeStr
                        );
                        String markdown = formatMarkdown(result);
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

    private String formatMarkdown(JdkBugReferenceResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# JDK Bug Cross-Reference\n\n");

        result.jvmVersion().ifPresent(v -> sb.append("**JVM Version:** ").append(v).append("\n\n"));

        if (!result.hasMatches()) {
            sb.append("## ✅ No Known JDK Bug Matches\n\n");
            sb.append("No events in this recording match known JDK bug signatures.\n\n");
            sb.append("Analyzed:\n");
            sb.append("- Compiler failures: ").append(result.compilerFailureCount()).append("\n");
            sb.append("- Java errors: ").append(result.errorCount()).append("\n");
            sb.append("- Biased lock revocations: ").append(result.biasedLockRevocationCount()).append("\n");
            return sb.toString();
        }

        sb.append("## ⚠️ ").append(result.matches().size())
                .append(" Potential JDK Bug Match").append(result.matches().size() > 1 ? "es" : "").append("\n\n");

        for (var match : result.matches()) {
            var bug = match.bug();
            sb.append("### ").append(bug.id()).append("\n\n");
            sb.append("| Field | Value |\n");
            sb.append("|-------|-------|\n");
            sb.append("| Severity | ").append(bug.severity()).append(" |\n");
            sb.append("| Category | ").append(bug.category()).append(" |\n");
            sb.append("| Matched Pattern | `").append(match.matchedText()).append("` |\n");
            sb.append("| Affected Versions | ").append(String.join(", ", bug.affectedVersions())).append(" |\n");
            if (bug.fixedIn() != null) {
                sb.append("| Fixed In | ").append(bug.fixedIn()).append(" |\n");
            }
            if (match.versionAffected()) {
                sb.append("| **Your Version** | ⚠️ **AFFECTED** |\n");
            } else if (result.jvmVersion().isPresent()) {
                sb.append("| **Your Version** | Likely not affected |\n");
            }
            if (bug.workaround() != null) {
                sb.append("| Workaround | `").append(bug.workaround()).append("` |\n");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
