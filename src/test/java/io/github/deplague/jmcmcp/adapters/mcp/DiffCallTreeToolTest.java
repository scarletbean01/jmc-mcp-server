package io.github.deplague.jmcmcp.adapters.mcp;

import io.github.deplague.jmcmcp.adapters.infrastructure.JfrProviderImpl;
import io.github.deplague.jmcmcp.application.service.DiffCallTreeApplicationService;
import io.github.deplague.jmcmcp.application.service.ExpandDiffCallTreeApplicationService;
import io.github.deplague.jmcmcp.domain.service.DiffCallTreeService;
import io.github.deplague.jmcmcp.domain.service.ExpandDiffCallTreeService;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.CallTreeCache;
import io.github.deplague.jmcmcp.adapters.infrastructure.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.adapters.infrastructure.security.RecordingAccessController;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DiffCallTreeToolTest {

    private static String afterPath;
    private static String beforePath;

    private JfrRecordingCache cache;
    private DiffCallTreeApplicationService appService;
    private DiffCallTreeTool tool;

    @BeforeAll
    static void resolveJfrFiles() {
        afterPath = resolveJfr("after.jfr");
        beforePath = resolveJfr("before.jfr");
    }

    private static String resolveJfr(String name) {
        File file = new File(name);
        if (!file.exists()) {
            file = new File(System.getProperty("user.dir"), name);
        }
        assertThat(file).exists();
        return file.getAbsolutePath();
    }

    @BeforeEach
    void setUp() {
        cache = new JfrRecordingCache();
        RecordingAccessController accessController = new RecordingAccessController();
        JfrProviderImpl jfrProvider = new JfrProviderImpl(cache, accessController);
        DiffCallTreeService domainService = new DiffCallTreeService();
        CallTreeCache callTreeCache = new CallTreeCache();
        appService = new DiffCallTreeApplicationService(jfrProvider, domainService, callTreeCache);
        tool = new DiffCallTreeTool(appService);
    }

    @Test
    void getDiffTreeReturnsTreeIdAndNodes() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_get_diff_tree", Map.of(
                        "baseline_jfr_path", beforePath,
                        "target_jfr_path", afterPath,
                        "subsystem", "cpu")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Diff Call Tree");
        assertThat(text).contains("Tree ID:");
        assertThat(text).contains("Baseline Samples:");
        assertThat(text).contains("Target Samples:");
        assertThat(text).contains("| Node ID | Method | Baseline | Target | Δ | Baseline % | Target % | Change |");
    }

    @Test
    void getDiffTreeWithSocketSubsystem() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_get_diff_tree", Map.of(
                        "baseline_jfr_path", beforePath,
                        "target_jfr_path", afterPath,
                        "subsystem", "socket")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Diff Call Tree");
    }

    @Test
    void getDiffTreeWithFileSubsystem() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_get_diff_tree", Map.of(
                        "baseline_jfr_path", beforePath,
                        "target_jfr_path", afterPath,
                        "subsystem", "file")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Diff Call Tree");
    }

    @Test
    void getDiffTreeWithLockSubsystem() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_get_diff_tree", Map.of(
                        "baseline_jfr_path", beforePath,
                        "target_jfr_path", afterPath,
                        "subsystem", "lock")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Diff Call Tree");
    }

    @Test
    void getDiffTreeWithPackageFilter() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_get_diff_tree", Map.of(
                        "baseline_jfr_path", beforePath,
                        "target_jfr_path", afterPath,
                        "subsystem", "cpu",
                        "package_filter", "java.lang")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("Package Filter:");
    }

    @Test
    void getDiffTreeReturnsErrorForMissingBaseline() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_get_diff_tree", Map.of(
                        "baseline_jfr_path", "/nonexistent/baseline.jfr",
                        "target_jfr_path", afterPath,
                        "subsystem", "cpu")));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void getDiffTreeReturnsErrorForMissingTarget() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_get_diff_tree", Map.of(
                        "baseline_jfr_path", beforePath,
                        "target_jfr_path", "/nonexistent/target.jfr",
                        "subsystem", "cpu")));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void getDiffTreeReturnsErrorForMissingArgument() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_get_diff_tree", Map.of()));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Missing required argument");
    }

    @Test
    void getDiffTreeCachesTreeInCallTreeCache() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_get_diff_tree", Map.of(
                        "baseline_jfr_path", beforePath,
                        "target_jfr_path", afterPath,
                        "subsystem", "cpu")));

        assertThat(result.isError()).isFalse();
        assertThat(appService.getCallTreeCache().getDiffTreeCount()).isGreaterThan(0);
    }

    @Test
    void expandDiffNodeReturnsChildren() {
        CallToolResult treeResult = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_get_diff_tree", Map.of(
                        "baseline_jfr_path", beforePath,
                        "target_jfr_path", afterPath,
                        "subsystem", "cpu")));

        String treeText = extractText(treeResult);
        String treeId = extractTreeId(treeText);

        ExpandDiffCallTreeService expandDomainService = new ExpandDiffCallTreeService();
        ExpandDiffCallTreeApplicationService expandAppService = new ExpandDiffCallTreeApplicationService(appService.getCallTreeCache(), expandDomainService);
        ExpandDiffCallTreeTool expandTool = new ExpandDiffCallTreeTool(expandAppService);
        CallToolResult expandResult = expandTool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("expand_diff_call_tree", Map.of(
                        "tree_id", treeId,
                        "node_id", "root-0")));

        assertThat(expandResult.isError()).isFalse();
        String expandText = extractText(expandResult);
        assertThat(expandText).contains("# Expanded Diff Node:");
        assertThat(expandText).contains("Node ID | Method | Baseline | Target | Δ | Baseline % | Target % | Change |");
    }

    @Test
    void expandDiffNodeReturnsErrorForExpiredTree() {
        ExpandDiffCallTreeService expandDomainService = new ExpandDiffCallTreeService();
        ExpandDiffCallTreeApplicationService expandAppService = new ExpandDiffCallTreeApplicationService(appService.getCallTreeCache(), expandDomainService);
        ExpandDiffCallTreeTool expandTool = new ExpandDiffCallTreeTool(expandAppService);
        CallToolResult result = expandTool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("expand_diff_call_tree", Map.of(
                        "tree_id", "nonexistent-tree-id",
                        "node_id", "root-0")));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Diff tree not found or expired");
    }

    @Test
    void expandDiffNodeReturnsErrorForInvalidNodeId() {
        CallToolResult treeResult = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("smart_get_diff_tree", Map.of(
                        "baseline_jfr_path", beforePath,
                        "target_jfr_path", afterPath,
                        "subsystem", "cpu")));

        String treeId = extractTreeId(extractText(treeResult));

        ExpandDiffCallTreeService expandDomainService = new ExpandDiffCallTreeService();
        ExpandDiffCallTreeApplicationService expandAppService = new ExpandDiffCallTreeApplicationService(appService.getCallTreeCache(), expandDomainService);
        ExpandDiffCallTreeTool expandTool = new ExpandDiffCallTreeTool(expandAppService);
        CallToolResult result = expandTool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("expand_diff_call_tree", Map.of(
                        "tree_id", treeId,
                        "node_id", "root-999999")));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("not found in diff tree");
    }

    private static String extractText(CallToolResult result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }

    private static String extractTreeId(String text) {
        int start = text.indexOf("Tree ID:`");
        if (start < 0) start = text.indexOf("Tree ID: `");
        if (start < 0) {
            // fallback: first backtick-enclosed UUID-like string
            start = text.indexOf("`");
        }
        int end = text.indexOf("`", start + 1);
        if (start >= 0 && end > start) {
            return text.substring(start + 1, end);
        }
        return "";
    }
}
