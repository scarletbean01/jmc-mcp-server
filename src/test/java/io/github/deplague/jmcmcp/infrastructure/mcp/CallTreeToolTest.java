package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.infrastructure.jfr.JfrProviderImpl;
import io.github.deplague.jmcmcp.infrastructure.jfr.CallTreeCache;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.infrastructure.mcp.CallTreeTool;
import io.github.deplague.jmcmcp.infrastructure.mcp.ExpandCallTreeTool;
import io.github.deplague.jmcmcp.infrastructure.security.RecordingAccessController;
import io.github.deplague.jmcmcp.application.service.CallTreeApplicationService;
import io.github.deplague.jmcmcp.application.service.ExpandCallTreeApplicationService;
import io.github.deplague.jmcmcp.domain.service.CallTreeService;
import io.github.deplague.jmcmcp.domain.service.ExpandCallTreeService;
import io.quarkiverse.mcp.server.TextContent;
import io.quarkiverse.mcp.server.ToolResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class CallTreeToolTest {

    private static String afterPath;

    private JfrRecordingCache cache;
    private CallTreeApplicationService appService;
    private CallTreeTool tool;

    @BeforeAll
    static void resolveJfrFiles() {
        afterPath = resolveJfr("after.jfr");
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
        CallTreeService domainService = new CallTreeService();
        CallTreeCache callTreeCache = new CallTreeCache();
        appService = new CallTreeApplicationService(jfrProvider, domainService, callTreeCache);
        tool = new CallTreeTool(appService);
    }

    @Test
    void getCallTreeReturnsTreeIdAndNodes() {
        ToolResponse result = tool.callTree(afterPath, "cpu", null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Call Tree");
        assertThat(text).contains("Tree ID:");
        assertThat(text).contains("Total Samples:");
        assertThat(text).contains("| Node ID | Method | Self Samples | Total Samples | Self % | Total % | Children? |");
    }

    @Test
    void getCallTreeWithSocketSubsystem() {
        ToolResponse result = tool.callTree(afterPath, "socket", null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Call Tree");
    }

    @Test
    void getCallTreeWithFileSubsystem() {
        ToolResponse result = tool.callTree(afterPath, "file", null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Call Tree");
    }

    @Test
    void getCallTreeWithLockSubsystem() {
        ToolResponse result = tool.callTree(afterPath, "lock", null, null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Call Tree");
    }

    @Test
    void getCallTreeWithPackageFilter() {
        ToolResponse result = tool.callTree(afterPath, "cpu", "java.lang", null, null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("Package Filter:");
    }

    @Test
    void getCallTreeWithTimeRange() {
        ToolResponse result = tool.callTree(afterPath, "cpu", null, "2025-01-01T00:00:00Z", "2099-12-31T23:59:59Z");

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Call Tree");
    }

    @Test
    void getCallTreeCachesResultOnSecondCall() {
        ToolResponse first = tool.callTree(afterPath, "cpu", null, null, null);
        ToolResponse second = tool.callTree(afterPath, "cpu", null, null, null);

        assertThat(first.isError()).isFalse();
        assertThat(second.isError()).isFalse();
        String firstText = extractText(first);
        String secondText = extractText(second);
        // Tree IDs are regenerated each call, but structure should be identical
        assertThat(firstText).contains("# Call Tree").contains("Tree ID:");
        assertThat(secondText).contains("# Call Tree").contains("Tree ID:");
    }

    @Test
    void getCallTreeReturnsErrorForMissingFile() {
        ToolResponse result = tool.callTree("/nonexistent/path.jfr", "cpu", null, null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error:");
    }

    @Test
    void getCallTreeReturnsErrorForMissingArgument() {
        ToolResponse result = tool.callTree(null, null, null, null, null);

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Error: Path cannot be null or blank");
    }

    @Test
    void getCallTreeCachesTreeInCallTreeCache() {
        ToolResponse result = tool.callTree(afterPath, "cpu", null, null, null);

        assertThat(result.isError()).isFalse();
        assertThat(appService.getCallTreeCache().size()).isGreaterThan(0);
    }

    @Test
    void expandNodeReturnsChildren() {
        // First create a tree
        ToolResponse treeResult = tool.callTree(afterPath, "cpu", null, null, null);

        String treeText = extractText(treeResult);
        String treeId = extractTreeId(treeText);

        ExpandCallTreeTool expandTool = new ExpandCallTreeTool(
                new ExpandCallTreeApplicationService(appService.getCallTreeCache(), new ExpandCallTreeService()));
        ToolResponse expandResult = expandTool.expandCallTree(treeId, "root-0");

        assertThat(expandResult.isError()).isFalse();
        String expandText = extractText(expandResult);
        assertThat(expandText).contains("# Expanded Node:");
        assertThat(expandText).contains("Node ID | Method | Self Samples | Total Samples | Self % | Total % | Children? |");
    }

    @Test
    void expandNodeReturnsErrorForExpiredTree() {
        ExpandCallTreeTool expandTool = new ExpandCallTreeTool(
                new ExpandCallTreeApplicationService(appService.getCallTreeCache(), new ExpandCallTreeService()));
        ToolResponse result = expandTool.expandCallTree("nonexistent-tree-id", "root-0");

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("Tree not found or expired");
    }

    @Test
    void expandNodeReturnsErrorForInvalidNodeId() {
        ToolResponse treeResult = tool.callTree(afterPath, "cpu", null, null, null);

        String treeId = extractTreeId(extractText(treeResult));

        ExpandCallTreeTool expandTool = new ExpandCallTreeTool(
                new ExpandCallTreeApplicationService(appService.getCallTreeCache(), new ExpandCallTreeService()));
        ToolResponse result = expandTool.expandCallTree(treeId, "root-999999");

        assertThat(result.isError()).isTrue();
        assertThat(extractText(result)).contains("not found in tree");
    }

    private static String extractText(ToolResponse result) {
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
