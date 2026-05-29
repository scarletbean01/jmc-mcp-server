package io.github.deplague.jmcmcp.infrastructure.jfr;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.openjdk.jmc.common.IMCMethod;
import org.openjdk.jmc.flightrecorder.stacktrace.tree.Node;
import org.openjdk.jmc.flightrecorder.stacktrace.tree.StacktraceTreeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * Cache for interactive call tree and diff tree models using Caffeine.
 *
 * <p>Maintains {@link StacktraceTreeModel} instances mapped to unique tree IDs,
 * with TTL and size-based eviction.</p>
 */
public final class CallTreeCache {

    private static final Logger LOG = LoggerFactory.getLogger(CallTreeCache.class);

    private static final long DEFAULT_TTL_MINUTES = 60;
    private static final int MAX_ENTRIES = 50;

    private final Cache<String, CachedTree> trees;
    private final Cache<String, CachedDiffTree> diffTrees;

    public CallTreeCache() {
        this(DEFAULT_TTL_MINUTES);
    }

    public CallTreeCache(long ttlMinutes) {
        this.trees = Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofMinutes(ttlMinutes))
                .maximumSize(MAX_ENTRIES)
                .build();
        
        this.diffTrees = Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofMinutes(ttlMinutes))
                .maximumSize(MAX_ENTRIES)
                .build();
                
        LOG.info("CallTreeCache initialized: TTL={}min, MaxSize={}", ttlMinutes, MAX_ENTRIES);
    }

    /**
     * Cache a single-recording tree and return its unique ID.
     */
    public String cacheTree(
            StacktraceTreeModel tree,
            String filePath,
            String subsystem,
            String packageFilter
    ) {
        String treeId = UUID.randomUUID().toString();
        double totalSamples = computeTotalSamples(tree.getRoot());
        trees.put(
                treeId,
                new CachedTree(
                        tree,
                        filePath,
                        subsystem,
                        packageFilter,
                        totalSamples,
                        Instant.now()
                )
        );
        LOG.debug("Cached tree: {} (totalSamples={})", treeId, totalSamples);
        return treeId;
    }

    /**
     * Retrieve a cached tree if present and not expired.
     */
    public CachedTree getTree(String treeId) {
        return trees.getIfPresent(treeId);
    }

    /**
     * Cache a diff tree and return its unique ID.
     */
    public String cacheDiffTree(
            DiffTreeNode root,
            String baselinePath,
            String targetPath,
            String subsystem,
            String packageFilter,
            double baselineTotalSamples,
            double targetTotalSamples
    ) {
        String treeId = UUID.randomUUID().toString();
        diffTrees.put(
                treeId,
                new CachedDiffTree(
                        root,
                        baselinePath,
                        targetPath,
                        subsystem,
                        packageFilter,
                        baselineTotalSamples,
                        targetTotalSamples,
                        Instant.now()
                )
        );
        LOG.debug(
                "Cached diff tree: {} (baseline={}, target={})",
                treeId,
                baselineTotalSamples,
                targetTotalSamples
        );
        return treeId;
    }

    /**
     * Retrieve a cached diff tree if present and not expired.
     */
    public CachedDiffTree getDiffTree(String treeId) {
        return diffTrees.getIfPresent(treeId);
    }

    /**
     * Look up a node in a single tree by its hierarchical path ID.
     *
     * @param root   the tree root
     * @param nodeId e.g. "root", "root-0", "root-0-2-1"
     * @return the matching node, or null if not found
     */
    public static Node findNode(Node root, String nodeId) {
        if ("root".equals(nodeId)) {
            return root;
        }
        String[] parts = nodeId.split("-");
        if (parts.length < 2 || !"root".equals(parts[0])) {
            return null;
        }
        Node current = root;
        for (int i = 1; i < parts.length; i++) {
            int index = Integer.parseInt(parts[i]);
            List<Node> children = current.getChildren();
            if (index < 0 || index >= children.size()) {
                return null;
            }
            current = children.get(index);
        }
        return current;
    }

    /**
     * Look up a node in a diff tree by its hierarchical path ID.
     */
    public static DiffTreeNode findDiffNode(DiffTreeNode root, String nodeId) {
        if ("root".equals(nodeId)) {
            return root;
        }
        String[] parts = nodeId.split("-");
        if (parts.length < 2 || !"root".equals(parts[0])) {
            return null;
        }
        DiffTreeNode current = root;
        for (int i = 1; i < parts.length; i++) {
            int index = Integer.parseInt(parts[i]);
            List<DiffTreeNode> children = current.children();
            if (index < 0 || index >= children.size()) {
                return null;
            }
            current = children.get(index);
        }
        return current;
    }

    /**
     * Compute the total number of unique samples in a tree.
     */
    public static double computeTotalSamples(Node root) {
        double total = 0;
        for (Node child : root.getChildren()) {
            total += child.getCumulativeWeight();
        }
        return total;
    }

    /**
     * Format a frame's method name for display.
     */
    public static String formatMethodName(Node node) {
        if (
                node == null ||
                        node.getFrame() == null ||
                        node.getFrame().getMethod() == null
        ) {
            return "Unknown";
        }
        IMCMethod method = node.getFrame().getMethod();
        String type =
                method.getType() != null
                        ? method.getType().getFullName()
                        : "Unknown";
        return type + "." + method.getMethodName() + "()";
    }

    /**
     * Format a frame's method name for display from a diff node.
     */
    public static String formatMethodName(DiffTreeNode node) {
        return node.methodName() != null ? node.methodName() : "Unknown";
    }

    /**
     * Check whether a node's frame matches the given package prefix.
     */
    public static boolean matchesPackageFilter(
            Node node,
            String packageFilter
    ) {
        if (packageFilter == null || packageFilter.isBlank()) {
            return true;
        }
        if (
                node == null ||
                        node.getFrame() == null ||
                        node.getFrame().getMethod() == null
        ) {
            return false;
        }
        IMCMethod method = node.getFrame().getMethod();
        String typeName =
                method.getType() != null ? method.getType().getFullName() : "";
        return typeName.startsWith(packageFilter);
    }

    /**
     * Get the visible children of a node, applying package-filter tree folding.
     * Non-matching nodes are bypassed and their matching descendants are surfaced.
     */
    public static List<Node> getVisibleChildren(
            Node node,
            String packageFilter
    ) {
        if (packageFilter == null || packageFilter.isBlank()) {
            return node.getChildren();
        }
        List<Node> result = new ArrayList<>();
        for (Node child : node.getChildren()) {
            if (matchesPackageFilter(child, packageFilter)) {
                result.add(child);
            } else {
                result.addAll(getVisibleChildren(child, packageFilter));
            }
        }
        return result;
    }

    /**
     * Get the visible children of a diff node, applying package-filter tree folding.
     */
    public static List<DiffTreeNode> getVisibleDiffChildren(
            DiffTreeNode node,
            String packageFilter
    ) {
        if (packageFilter == null || packageFilter.isBlank()) {
            return node.children();
        }
        List<DiffTreeNode> result = new ArrayList<>();
        for (DiffTreeNode child : node.children()) {
            if (matchesPackageFilter(child, packageFilter)) {
                result.add(child);
            } else {
                result.addAll(getVisibleDiffChildren(child, packageFilter));
            }
        }
        return result;
    }

    private static boolean matchesPackageFilter(
            DiffTreeNode node,
            String packageFilter
    ) {
        if (packageFilter == null || packageFilter.isBlank()) {
            return true;
        }
        if (node.methodName() == null) {
            return false;
        }
        int dotIdx = node.methodName().lastIndexOf('.');
        String typeName =
                dotIdx > 0
                        ? node.methodName().substring(0, dotIdx)
                        : node.methodName();
        return typeName.startsWith(packageFilter);
    }

    /**
     * @return number of cached single trees
     */
    public int size() {
        trees.cleanUp();
        return (int) trees.estimatedSize();
    }

    /**
     * @return number of cached diff trees
     */
    public int getDiffTreeCount() {
        diffTrees.cleanUp();
        return (int) diffTrees.estimatedSize();
    }

    /**
     * Shutdown no longer needed, retained for API compatibility.
     */
    public void shutdown() {
        // No-op
    }

    // ------------------------------------------------------------------
    // Cached tree wrappers
    // ------------------------------------------------------------------

    public record CachedTree(
            StacktraceTreeModel tree,
            String filePath,
            String subsystem,
            String packageFilter,
            double totalSamples,
            Instant createdAt
    ) {}

    public record CachedDiffTree(
            DiffTreeNode root,
            String baselinePath,
            String targetPath,
            String subsystem,
            String packageFilter,
            double baselineTotalSamples,
            double targetTotalSamples,
            Instant createdAt
    ) {}

    // ------------------------------------------------------------------
    // Diff tree node
    // ------------------------------------------------------------------

    /**
     * Immutable node in a diff call tree. Encapsulates baseline and target data
     * for a matched frame across two recordings.
     */
    public record DiffTreeNode(
            String methodName,
            double baselineWeight,
            double targetWeight,
            double baselineCumulative,
            double targetCumulative,
            String changeType,
            List<DiffTreeNode> children
    ) {
        public DiffTreeNode {
            children =
                    children != null
                            ? Collections.unmodifiableList(new ArrayList<>(children))
                            : Collections.emptyList();
        }

        public double totalBaseline() {
            return baselineCumulative;
        }

        public double totalTarget() {
            return targetCumulative;
        }

        public boolean hasChildren() {
            return !children.isEmpty();
        }

        public double delta() {
            return targetCumulative - baselineCumulative;
        }

        public double percentageChange() {
            if (baselineCumulative > 0) {
                return (
                        ((targetCumulative - baselineCumulative) /
                                baselineCumulative) * 100.0
                );
            }
            return targetCumulative > 0 ? Double.POSITIVE_INFINITY : 0.0;
        }
    }
}
