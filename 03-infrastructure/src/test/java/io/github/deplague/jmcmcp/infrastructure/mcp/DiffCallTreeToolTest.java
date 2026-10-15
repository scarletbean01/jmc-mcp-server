package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.infrastructure.jfr.JfrAccessorRepositoryImpl;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrProviderImpl;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrQuantityAggregatorImpl;
import io.github.deplague.jmcmcp.domain.service.CallTreeCache;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrRecordingCache;
import io.github.deplague.jmcmcp.infrastructure.security.RecordingAccessController;
import io.github.deplague.jmcmcp.application.service.DiffCallTreeApplicationService;
import io.github.deplague.jmcmcp.application.service.ExpandDiffCallTreeApplicationService;
import io.github.deplague.jmcmcp.domain.service.DiffCallTreeService;
import io.github.deplague.jmcmcp.domain.service.ExpandDiffCallTreeService;
import io.quarkiverse.mcp.server.TextContent;
import io.quarkiverse.mcp.server.ToolResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        JfrAccessorRepositoryImpl accessorRepo = new JfrAccessorRepositoryImpl();
        JfrQuantityAggregatorImpl quantityAgg = new JfrQuantityAggregatorImpl(accessorRepo);
        DiffCallTreeService domainService = new DiffCallTreeService(accessorRepo, quantityAgg);
        CallTreeCache callTreeCache = new CallTreeCache();
        appService = new DiffCallTreeApplicationService(jfrProvider, domainService, callTreeCache);
        tool = new DiffCallTreeTool(appService);
    }

    @Test
    void getDiffTreeReturnsTreeIdAndNodes() throws Exception {
        ToolResponse result = tool.diffCallTree(beforePath, afterPath, "cpu", null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Diff Call Tree");
        assertThat(text).contains("Tree ID:");
        assertThat(text).contains("Baseline Samples:");
        assertThat(text).contains("Target Samples:");
        assertThat(text).contains("| Node ID | Method | Baseline | Target | Δ | Baseline % | Target % | Change |");
    }

    @Test
    void getDiffTreeWithSocketSubsystem() throws Exception {
        ToolResponse result = tool.diffCallTree(beforePath, afterPath, "socket", null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Diff Call Tree");
    }

    @Test
    void getDiffTreeWithFileSubsystem() throws Exception {
        ToolResponse result = tool.diffCallTree(beforePath, afterPath, "file", null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Diff Call Tree");
    }

    @Test
    void getDiffTreeWithLockSubsystem() throws Exception {
        ToolResponse result = tool.diffCallTree(beforePath, afterPath, "lock", null);

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("# Diff Call Tree");
    }

    @Test
    void getDiffTreeWithPackageFilter() throws Exception {
        ToolResponse result = tool.diffCallTree(beforePath, afterPath, "cpu", "java.lang");

        assertThat(result.isError()).isFalse();
        String text = extractText(result);
        assertThat(text).contains("Package Filter:");
    }

    @Test
    void getDiffTreeReturnsErrorForMissingBaseline() throws Exception {
        assertThatThrownBy(() -> tool.diffCallTree("/nonexistent/baseline.jfr", afterPath, "cpu", null))
                .isInstanceOf(Exception.class);
    }

    @Test
    void getDiffTreeReturnsErrorForMissingTarget() throws Exception {
        assertThatThrownBy(() -> tool.diffCallTree(beforePath, "/nonexistent/target.jfr", "cpu", null))
                .isInstanceOf(Exception.class);
    }

    @Test
    void getDiffTreeReturnsErrorForMissingArgument() throws Exception {
        assertThatThrownBy(() -> tool.diffCallTree(null, null, null, null))
                .isInstanceOf(Exception.class);
    }

    @Test
    void getDiffTreeCachesTreeInCallTreeCache() throws Exception {
        ToolResponse result = tool.diffCallTree(beforePath, afterPath, "cpu", null);

        assertThat(result.isError()).isFalse();
        assertThat(appService.getCallTreeCache().getDiffTreeCount()).isGreaterThan(0);
    }

    @Test
    void expandDiffNodeReturnsChildren() throws Exception {
        ToolResponse treeResult = tool.diffCallTree(beforePath, afterPath, "cpu", null);

        String treeText = extractText(treeResult);
        String treeId = extractTreeId(treeText);

        ExpandDiffCallTreeService expandDomainService = new ExpandDiffCallTreeService();
        ExpandDiffCallTreeApplicationService expandAppService = new ExpandDiffCallTreeApplicationService(appService.getCallTreeCache(), expandDomainService);
        ExpandDiffCallTreeTool expandTool = new ExpandDiffCallTreeTool(expandAppService);
        ToolResponse expandResult = expandTool.expandDiffCallTree(treeId, "root-0");

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
        assertThatThrownBy(() -> expandTool.expandDiffCallTree("nonexistent-tree-id", "root-0"))
                .isInstanceOf(Exception.class);
    }

    @Test
    void expandDiffNodeReturnsErrorForInvalidNodeId() throws Exception {
        ToolResponse treeResult = tool.diffCallTree(beforePath, afterPath, "cpu", null);
        String treeId = extractTreeId(extractText(treeResult));

        ExpandDiffCallTreeService expandDomainService = new ExpandDiffCallTreeService();
        ExpandDiffCallTreeApplicationService expandAppService = new ExpandDiffCallTreeApplicationService(appService.getCallTreeCache(), expandDomainService);
        ExpandDiffCallTreeTool expandTool = new ExpandDiffCallTreeTool(expandAppService);
        assertThatThrownBy(() -> expandTool.expandDiffCallTree(treeId, "root-999999"))
                .isInstanceOf(Exception.class);
    }

    private static String extractText(ToolResponse result) {
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
