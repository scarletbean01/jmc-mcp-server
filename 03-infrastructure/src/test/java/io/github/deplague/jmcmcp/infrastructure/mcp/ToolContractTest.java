package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

class ToolContractTest {

    private static final String MCP_PACKAGE = "io.github.deplague.jmcmcp.infrastructure.mcp";
    private static final Set<Class<?>> TOOL_CLASSES = new HashSet<>();
    private static final Map<String, Method> REGISTERED_TOOLS = new HashMap<>();

    @BeforeAll
    static void discoverTools() throws Exception {
        String packagePath = MCP_PACKAGE.replace('.', '/');
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(packagePath);

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            if ("file".equals(resource.getProtocol())) {
                File dir = new File(resource.toURI());
                if (dir.exists() && dir.isDirectory()) {
                    File[] files = dir.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            String name = file.getName();
                            if (name.endsWith("Tool.class") && !name.contains("$")) {
                                String className = MCP_PACKAGE + "." + name.replace(".class", "");
                                Class<?> clazz = Class.forName(className);
                                if (clazz.isAnnotationPresent(ApplicationScoped.class)) {
                                    TOOL_CLASSES.add(clazz);
                                    for (Method method : clazz.getDeclaredMethods()) {
                                        Tool tool = method.getAnnotation(Tool.class);
                                        if (tool != null) {
                                            REGISTERED_TOOLS.put(tool.name(), method);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    void allToolsAreDiscoveredAndCountMatches() {
        assertThat(TOOL_CLASSES)
                .withFailMessage("Expected discovered tool classes to match ToolNames.ALL size (%d), found %d",
                        ToolNames.ALL.size(), TOOL_CLASSES.size())
                .hasSameSizeAs(ToolNames.ALL);

        assertThat(REGISTERED_TOOLS)
                .withFailMessage("Expected registered @Tool methods to match ToolNames.ALL size (%d), found %d",
                        ToolNames.ALL.size(), REGISTERED_TOOLS.size())
                .hasSameSizeAs(ToolNames.ALL);
    }

    @Test
    void everyToolClassHasApplicationScopedAndHandleToolError() {
        for (Class<?> clazz : TOOL_CLASSES) {
            assertThat(clazz.isAnnotationPresent(ApplicationScoped.class))
                    .withFailMessage("Tool class %s is missing @ApplicationScoped", clazz.getSimpleName())
                    .isTrue();

            assertThat(clazz.isAnnotationPresent(HandleToolError.class))
                    .withFailMessage("Tool class %s is missing @HandleToolError", clazz.getSimpleName())
                    .isTrue();
        }
    }

    @Test
    void everyToolMethodUsesVirtualThreadsAndNeverBlocking() {
        for (Map.Entry<String, Method> entry : REGISTERED_TOOLS.entrySet()) {
            String toolName = entry.getKey();
            Method method = entry.getValue();

            assertThat(method.isAnnotationPresent(RunOnVirtualThread.class))
                    .withFailMessage("Tool '%s' (%s.%s) must be annotated with @RunOnVirtualThread",
                            toolName, method.getDeclaringClass().getSimpleName(), method.getName())
                    .isTrue();

            assertThat(method.isAnnotationPresent(Blocking.class))
                    .withFailMessage("Tool '%s' (%s.%s) must NOT be annotated with @Blocking",
                            toolName, method.getDeclaringClass().getSimpleName(), method.getName())
                    .isFalse();
        }
    }

    @Test
    void toolNamesFollowSnakeCaseConventionAndMatchRegistry() {
        Pattern snakeCasePattern = Pattern.compile("^[a-z][a-z0-9_]*$");

        for (String toolName : REGISTERED_TOOLS.keySet()) {
            assertThat(snakeCasePattern.matcher(toolName).matches())
                    .withFailMessage("Tool name '%s' does not follow snake_case convention", toolName)
                    .isTrue();

            assertThat(ToolNames.ALL)
                    .withFailMessage("Tool name '%s' is not registered in ToolNames.ALL", toolName)
                    .contains(toolName);
        }

        assertThat(ToolNames.ALL)
                .withFailMessage("ToolNames.ALL has %d tools, but %d are registered",
                        ToolNames.ALL.size(), REGISTERED_TOOLS.size())
                .hasSameSizeAs(REGISTERED_TOOLS.keySet());
    }

    @Test
    void noPrimitiveTypesInOptionalToolArguments() {
        for (Map.Entry<String, Method> entry : REGISTERED_TOOLS.entrySet()) {
            String toolName = entry.getKey();
            Method method = entry.getValue();

            for (Parameter param : method.getParameters()) {
                ToolArg arg = param.getAnnotation(ToolArg.class);
                if (arg != null && !arg.required()) {
                    assertThat(param.getType().isPrimitive())
                            .withFailMessage("Tool '%s' parameter '%s' has optional @ToolArg but uses primitive type '%s'",
                                    toolName, arg.name(), param.getType().getSimpleName())
                            .isFalse();
                }
            }
        }
    }

    @Test
    void standardToolArgDescriptionsMatchToolDocs() {
        for (Map.Entry<String, Method> entry : REGISTERED_TOOLS.entrySet()) {
            String toolName = entry.getKey();
            Method method = entry.getValue();

            for (Parameter param : method.getParameters()) {
                ToolArg arg = param.getAnnotation(ToolArg.class);
                if (arg == null) continue;

                switch (arg.name()) {
                    case "jfr_file_path", "recording_path" -> assertThat(arg.description())
                            .withFailMessage("Tool '%s' parameter '%s' description mismatch", toolName, arg.name())
                            .isEqualTo(ToolDocs.JFR_FILE_PATH);
                    case "heap_dump_path" -> assertThat(arg.description())
                            .withFailMessage("Tool '%s' parameter '%s' description mismatch", toolName, arg.name())
                            .isEqualTo(ToolDocs.HEAP_DUMP_PATH);
                    case "start_time" -> assertThat(arg.description())
                            .withFailMessage("Tool '%s' parameter '%s' description mismatch", toolName, arg.name())
                            .isEqualTo(ToolDocs.START_TIME);
                    case "end_time" -> assertThat(arg.description())
                            .withFailMessage("Tool '%s' parameter '%s' description mismatch", toolName, arg.name())
                            .isEqualTo(ToolDocs.END_TIME);
                    case "package_prefix" -> assertThat(arg.description())
                            .withFailMessage("Tool '%s' parameter '%s' description mismatch", toolName, arg.name())
                            .isEqualTo(ToolDocs.PACKAGE_PREFIX);
                    case "top_n" -> assertThat(arg.description())
                            .withFailMessage("Tool '%s' parameter '%s' description mismatch", toolName, arg.name())
                            .isIn(ToolDocs.TOP_N_10, ToolDocs.TOP_N_20, ToolDocs.TOP_N_50);
                }
            }
        }
    }

    @Test
    void agentHintsReferenceRealToolNames() throws Exception {
        File srcDir = new File("src/main/java/io/github/deplague/jmcmcp/infrastructure/mcp");
        if (!srcDir.exists()) {
            srcDir = new File("03-infrastructure/src/main/java/io/github/deplague/jmcmcp/infrastructure/mcp");
        }
        assertThat(srcDir).isDirectory();

        Set<String> knownNonToolBackticks = Set.of(
                "tree_id", "node_id", "package_prefix", "class_pattern", "event_type",
                "start_time", "end_time", "top_n", "objectId", "jfr_file_path"
        );

        Pattern hintPattern = Pattern.compile("<agent_hint>(.*?)</agent_hint>", Pattern.DOTALL);
        Pattern backtickPattern = Pattern.compile("`([a-z0-9_]+)`");

        File[] files = srcDir.listFiles((dir, name) -> name.endsWith("Tool.java"));
        assertThat(files).isNotEmpty();

        for (File file : files) {
            String content = Files.readString(file.toPath());
            Matcher hintMatcher = hintPattern.matcher(content);
            while (hintMatcher.find()) {
                String hint = hintMatcher.group(1);
                Matcher btMatcher = backtickPattern.matcher(hint);
                while (btMatcher.find()) {
                    String identifier = btMatcher.group(1);
                    if (!knownNonToolBackticks.contains(identifier)
                            && (identifier.contains("_") || "correlate".equals(identifier))) {
                        assertThat(ToolNames.ALL)
                                .withFailMessage("File %s references unknown tool '%s' inside <agent_hint>",
                                        file.getName(), identifier)
                                .contains(identifier);
                    }
                }
            }
        }
    }
}
