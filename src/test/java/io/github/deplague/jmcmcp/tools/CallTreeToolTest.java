package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrRecordingCache;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CallTreeToolTest {

    private static String afterPath;
    private static String beforePath;

    private JfrRecordingCache cache;
    private JfrAnalysisService service;
    private CallTreeTool tool;

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
        service = new JfrAnalysisService(cache);
        tool = new CallTreeTool(service);
    }

    @Test
    void getCallTreeReturnsTreeIdAndNodes() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("get_call_tree", Map.of(
                        "jfr_file_path", afterPath,
                        "subsystem", "cpu")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Call Tree");
        assertThat(text).contains("Tree ID:");
        assertThat(text).contains("Total Samples:");
        assertThat(text).contains("| Node ID | Method | Self Samples | Total Samples | Self % | Total % | Children? |");
    }

    @Test
    void getCallTreeWithSocketSubsystem() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("get_call_tree", Map.of(
                        "jfr_file_path", afterPath,
                        "subsystem", "socket")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Call Tree");
    }

    @Test
    void getCallTreeWithFileSubsystem() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("get_call_tree", Map.of(
                        "jfr_file_path", afterPath,
                        "subsystem", "file")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Call Tree");
    }

    @Test
    void getCallTreeWithLockSubsystem() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("get_call_tree", Map.of(
                        "jfr_file_path", afterPath,
                        "subsystem", "lock")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Call Tree");
    }

    @Test
    void getCallTreeWithPackageFilter() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("get_call_tree", Map.of(
                        "jfr_file_path", afterPath,
                        "subsystem", "cpu",
                        "package_filter", "java.lang")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("Package Filter:");
    }

    @Test
    void getCallTreeWithTimeRange() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("get_call_tree", Map.of(
                        "jfr_file_path", afterPath,
                        "subsystem", "cpu",
                        "start_time", "2025-01-01T00:00:00Z",
                        "end_time", "2099-12-31T23:59:59Z")));

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Call Tree");
    }

    @Test
    void getCallTreeCachesResultOnSecondCall() {
        McpSchema.CallToolRequest request = new McpSchema.CallToolRequest("get_call_tree", Map.of(
                "jfr_file_path", afterPath,
                "subsystem", "cpu"));

        CallToolResult first = tool.spec().callHandler().apply(null, request);
        CallToolResult second = tool.spec().callHandler().apply(null, request);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        assertThat(extractText(first)).isEqualTo(extractText(second));
    }

    @Test
    void getCallTreeReturnsErrorForMissingFile() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("get_call_tree", Map.of(
                        "jfr_file_path", "/nonexistent/path.jfr",
                        "subsystem", "cpu")));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void getCallTreeReturnsErrorForMissingArgument() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("get_call_tree", Map.of()));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Missing required argument");
    }

    @Test
    void getCallTreeCachesTreeInCallTreeCache() {
        CallToolResult result = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("get_call_tree", Map.of(
                        "jfr_file_path", afterPath,
                        "subsystem", "cpu")));

        assertThat(result.isError()).isFalse();
        assertThat(tool.getCallTreeCache().size()).isGreaterThan(0);
    }

    @Test
    void expandNodeReturnsChildren() {
        // First create a tree
        CallToolResult treeResult = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("get_call_tree", Map.of(
                        "jfr_file_path", afterPath,
                        "subsystem", "cpu")));

        String treeText = extractText(treeResult);
        String treeId = extractTreeId(treeText);

        ExpandCallTreeTool expandTool = new ExpandCallTreeTool(tool.getCallTreeCache());
        CallToolResult expandResult = expandTool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("expand_node", Map.of(
                        "tree_id", treeId,
                        "node_id", "root-0")));

        assertThat(expandResult.isError()).isFalse();
        String expandText = extractText(expandResult);
        assertThat(expandText).contains("# Expanded Node:");
        assertThat(expandText).contains("Node ID | Method | Self Samples | Total Samples | Self % | Total % | Children? |");
    }

    @Test
    void expandNodeReturnsErrorForExpiredTree() {
        ExpandCallTreeTool expandTool = new ExpandCallTreeTool(tool.getCallTreeCache());
        CallToolResult result = expandTool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("expand_node", Map.of(
                        "tree_id", "nonexistent-tree-id",
                        "node_id", "root-0")));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Tree not found or expired");
    }

    @Test
    void expandNodeReturnsErrorForInvalidNodeId() {
        CallToolResult treeResult = tool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("get_call_tree", Map.of(
                        "jfr_file_path", afterPath,
                        "subsystem", "cpu")));

        String treeId = extractTreeId(extractText(treeResult));

        ExpandCallTreeTool expandTool = new ExpandCallTreeTool(tool.getCallTreeCache());
        CallToolResult result = expandTool.spec().callHandler().apply(null,
                new McpSchema.CallToolRequest("expand_node", Map.of(
                        "tree_id", treeId,
                        "node_id", "root-999999")));

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("not found in tree");
    }

    private static String extractText(CallToolResult result) {
        return result.content().stream()
                .filter(c -> c instanceof TextContent)
                .map(c -> ((TextContent) c).text())
                .findFirst()
                .orElse("");
    }

    private static String extractTreeId(String text) {
        int start = text.indexOf("`");
        int end = text.indexOf("`", start + 1);
        if (start >= 0 && end > start) {
            return text.substring(start + 1, end);
        }
        return "";
    }
}
