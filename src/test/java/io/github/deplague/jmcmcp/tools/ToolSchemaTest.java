package io.github.deplague.jmcmcp.tools;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

class ToolSchemaTest {

    /**
     * Verifies that all tool classes in the tools and adapters.mcp packages
     * have unique NAME values and that the count matches the number of tools
     * registered in JmcMcpServer.
     */
    @Test
    void toolNamesAreUniqueAndMatchServerRegistration() throws Exception {
        // 1. Discover all *Tool classes in the tools and adapters.mcp packages
        Set<String> toolClassNames = discoverToolClasses();

        // 2. Extract NAME field from each tool class
        Set<String> names = new HashSet<>();
        for (String className : toolClassNames) {
            Class<?> clazz = Class.forName(className);
            Field nameField = clazz.getDeclaredField("NAME");
            nameField.setAccessible(true);
            String name = (String) nameField.get(null);
            assertThat(name)
                    .withFailMessage("Tool %s has empty or null NAME", className)
                    .isNotNull()
                    .isNotEmpty();
            names.add(name);
        }

        // 3. Assert uniqueness (Set size equals class count)
        assertThat(names)
                .withFailMessage("Duplicate tool names found among %d tools", toolClassNames.size())
                .hasSize(toolClassNames.size());

        // 4. Assert count matches JmcMcpServer registration
        int registeredCount = countRegisteredTools();
        assertThat(toolClassNames.size())
                .withFailMessage(
                        "Tool class count (%d) does not match JmcMcpServer registration count (%d). " +
                        "Did you forget to register a new tool in JmcMcpServer?",
                        toolClassNames.size(), registeredCount)
                .isEqualTo(registeredCount);
    }

    /**
     * Verifies that no tool name contains spaces or uppercase letters (snake_case convention).
     */
    @Test
    void toolNamesFollowSnakeCaseConvention() throws Exception {
        Set<String> toolClassNames = discoverToolClasses();
        Pattern snakeCasePattern = Pattern.compile("^[a-z][a-z0-9_]*$");

        for (String className : toolClassNames) {
            Class<?> clazz = Class.forName(className);
            Field nameField = clazz.getDeclaredField("NAME");
            nameField.setAccessible(true);
            String name = (String) nameField.get(null);

            assertThat(snakeCasePattern.matcher(name).matches())
                    .withFailMessage("Tool %s NAME '%s' does not follow snake_case convention", className, name)
                    .isTrue();
        }
    }

    /**
     * Discovers all *Tool classes in the io.github.deplague.jmcmcp.tools
     * and io.github.deplague.jmcmcp.adapters.mcp packages by scanning the
     * classpath for .class files.
     */
    private Set<String> discoverToolClasses() throws Exception {
        Set<String> toolClasses = new TreeSet<>();

        String[] packageNames = {
                "io.github.deplague.jmcmcp.tools",
                "io.github.deplague.jmcmcp.adapters.mcp"
        };

        for (String packageName : packageNames) {
            String packagePath = packageName.replace('.', '/');

            java.util.Enumeration<java.net.URL> resources =
                    Thread.currentThread().getContextClassLoader().getResources(packagePath);

            while (resources.hasMoreElements()) {
                java.net.URL resource = resources.nextElement();
                if ("file".equals(resource.getProtocol())) {
                    java.io.File dir = new java.io.File(resource.toURI());
                    if (dir.exists() && dir.isDirectory()) {
                        for (java.io.File file : dir.listFiles()) {
                            String fileName = file.getName();
                            if (fileName.endsWith(".class") && !fileName.contains("$")) {
                                String className = packageName + "." + fileName.replace(".class", "");
                                // Only include actual tool classes (those with a NAME field)
                                try {
                                    Class<?> clazz = Class.forName(className);
                                    if (clazz.getDeclaredField("NAME") != null) {
                                        toolClasses.add(className);
                                    }
                                } catch (NoSuchFieldException e) {
                                    // Not a tool class (e.g., SchemaUtil, McpTool)
                                }
                            }
                        }
                    }
                } else if ("jar".equals(resource.getProtocol())) {
                    // Handle JAR scanning
                    String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));
                    try (java.util.jar.JarFile jarFile = new java.util.jar.JarFile(java.net.URLDecoder.decode(jarPath, "UTF-8"))) {
                        java.util.Enumeration<java.util.jar.JarEntry> entries = jarFile.entries();
                        while (entries.hasMoreElements()) {
                            java.util.jar.JarEntry entry = entries.nextElement();
                            String entryName = entry.getName();
                            if (entryName.startsWith(packagePath + "/")
                                    && entryName.endsWith(".class")
                                    && !entryName.contains("$")
                                    && entryName.split("/").length == packagePath.split("/").length + 1) {
                                String className = entryName.replace('/', '.').replace(".class", "");
                                try {
                                    Class<?> clazz = Class.forName(className);
                                    if (clazz.getDeclaredField("NAME") != null) {
                                        toolClasses.add(className);
                                    }
                                } catch (NoSuchFieldException e) {
                                    // Not a tool class
                                }
                            }
                        }
                    }
                }
            }
        }

        return toolClasses;
    }

    /**
     * Counts legacy tool instantiations (new *Tool(...) and CDI-discovered McpTools.
     */
    private int countRegisteredTools() throws Exception {
        String serverPath = System.getProperty("user.dir") + "/src/main/java/io/github/deplague/jmcmcp/JmcMcpServer.java";
        int count = 0;
        Pattern legacyPattern = Pattern.compile("new\\s+(\\w+Tool)\\s*\\(");

        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(serverPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = legacyPattern.matcher(line);
                if (matcher.find()) {
                    count++;
                }
            }
        }

        // Add CDI-discovered tools in adapters.mcp
        count += countCdiToolAdapters();
        return count;
    }

    private int countCdiToolAdapters() throws Exception {
        String packageName = "io.github.deplague.jmcmcp.adapters.mcp";
        String packagePath = packageName.replace('.', '/');
        int count = 0;

        java.util.Enumeration<java.net.URL> resources =
                Thread.currentThread().getContextClassLoader().getResources(packagePath);

        while (resources.hasMoreElements()) {
            java.net.URL resource = resources.nextElement();
            if ("file".equals(resource.getProtocol())) {
                java.io.File dir = new java.io.File(resource.toURI());
                if (dir.exists() && dir.isDirectory()) {
                    for (java.io.File file : dir.listFiles()) {
                        String fileName = file.getName();
                        if (fileName.endsWith(".class") && !fileName.contains("$")) {
                            String className = packageName + "." + fileName.replace(".class", "");
                            try {
                                Class<?> clazz = Class.forName(className);
                                if (clazz.getDeclaredField("NAME") != null) {
                                    count++;
                                }
                            } catch (NoSuchFieldException e) {
                                // Not a tool class
                            }
                        }
                    }
                }
            } else if ("jar".equals(resource.getProtocol())) {
                String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));
                try (java.util.jar.JarFile jarFile = new java.util.jar.JarFile(java.net.URLDecoder.decode(jarPath, "UTF-8"))) {
                    java.util.Enumeration<java.util.jar.JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        java.util.jar.JarEntry entry = entries.nextElement();
                        String entryName = entry.getName();
                        if (entryName.startsWith(packagePath + "/")
                                && entryName.endsWith(".class")
                                && !entryName.contains("$")
                                && entryName.split("/").length == packagePath.split("/").length + 1) {
                            String className = entryName.replace('/', '.').replace(".class", "");
                            try {
                                Class<?> clazz = Class.forName(className);
                                if (clazz.getDeclaredField("NAME") != null) {
                                    count++;
                                }
                            } catch (NoSuchFieldException e) {
                                // Not a tool class
                            }
                        }
                    }
                }
            }
        }
        return count;
    }
}
