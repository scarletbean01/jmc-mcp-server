package io.github.deplague.jmcmcp.tools;

import io.github.deplague.jmcmcp.jfr.JfrAnalysisService;
import io.github.deplague.jmcmcp.jfr.JfrItemUtils;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.openjdk.jmc.common.item.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class JdkBugReferenceTool {

    private static final String NAME = "jdk_bug_reference";

    private static final List<BugSignature> BUG_DATABASE = List.of(
            new BugSignature("JDK-8214231", "C2 compiler crash.*PhaseIdealLoop", List.of("11.", "17.0.0", "17.0.1", "17.0.2"),
                    "17.0.3", "-XX:CompileCommand=exclude,com/example/Class::method", "CRITICAL", "COMPILER"),
            new BugSignature("JDK-8231223", "java.lang.OutOfMemoryError.*Direct buffer", List.of("11.", "17."),
                    null, "-XX:MaxDirectMemorySize=512m", "HIGH", "MEMORY"),
            new BugSignature("JDK-8159193", "BiasedLockRevocation", List.of("11.", "17."),
                    null, "-XX:-UseBiasedLocking", "MEDIUM", "LOCKING"),
            new BugSignature("JDK-8245266", "G1GC.*assert.*should_not_be_here", List.of("11.", "15."),
                    "16+", "-XX:+UseZGC", "CRITICAL", "GC"),
            new BugSignature("JDK-8268060", "InternalError.*sigsegv.*PhaseIdealLoop", List.of("11.", "17.0.0", "17.0.1"),
                    "17.0.2", "-XX:TieredStopAtLevel=1", "CRITICAL", "COMPILER"),
            new BugSignature("JDK-8285916", "StackOverflowError.*C2.*compile", List.of("17.0.0", "17.0.1", "17.0.2"),
                    "17.0.3", "-XX:CompileCommand=exclude,*::*", "HIGH", "COMPILER"),
            new BugSignature("JDK-8297970", "ZGC.*heap.*allocation.*failure", List.of("17.", "19.", "20."),
                    "21+", null, "HIGH", "GC"),
            new BugSignature("JDK-8277044", "OutOfMemoryError.*Metaspace", List.of("11.", "17."),
                    null, "-XX:MaxMetaspaceSize=512m", "HIGH", "MEMORY"),
            new BugSignature("JDK-8256829", "CompilerFailure.*C2.*infinite loop", List.of("11.", "15.", "16."),
                    "17+", "-XX:TieredStopAtLevel=1", "HIGH", "COMPILER"),
            new BugSignature("JDK-8307424", "VirtualThread.*pin.*synchronized", List.of("21.", "22.0.0"),
                    "22.0.1", "Replace synchronized with ReentrantLock", "MEDIUM", "VIRTUAL_THREADS"),
            new BugSignature("JDK-8313206", "VirtualThread.*carrier.*starvation", List.of("21.", "22."),
                    null, "Increase -Djdk.virtualThreadScheduler.parallelism", "MEDIUM", "VIRTUAL_THREADS"),
            new BugSignature("JDK-8267693", "G1GC.*humongous.*allocation.*failure", List.of("11.", "15.", "16."),
                    "17+", "-XX:G1HeapRegionSize=32m", "HIGH", "GC"),
            new BugSignature("JDK-8240576", "InternalError.*C2.*node.*dominates", List.of("11.", "14.", "15."),
                    "16+", "-XX:CompileCommand=exclude,*::*", "CRITICAL", "COMPILER"),
            new BugSignature("JDK-8285972", "OutOfMemoryError.*GC overhead", List.of("11.", "17."),
                    null, "Increase -Xmx or reduce heap pressure", "HIGH", "GC"),
            new BugSignature("JDK-8235897", "assert.*failed.*PhaseChaitin", List.of("11.", "13.", "14."),
                    "15+", "-XX:TieredStopAtLevel=1", "CRITICAL", "COMPILER")
    );

    private final JfrAnalysisService service;

    public JdkBugReferenceTool(JfrAnalysisService service) {
        this.service = service;
    }

    public SyncToolSpecification spec() {
        return SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder()
                        .name(NAME)
                        .description("Cross-reference JFR events against known JDK bug signatures. " +
                                "Matches CompilerFailure, InternalError, and other events to known OpenJDK bugs " +
                                "and provides version-specific workarounds.")
                        .inputSchema(SchemaUtil.objectSchema(
                                SchemaUtil.props(
                                        "jfr_file_path", SchemaUtil.jfrFileProp(),
                                        "start_time", SchemaUtil.startTimeProp(),
                                        "end_time", SchemaUtil.endTimeProp()
                                ),
                                SchemaUtil.required("jfr_file_path")
                        ))
                        .build())
                .callHandler((exchange, request) -> {
                    try {
                        String filePath = SchemaUtil.getString(request.arguments(), "jfr_file_path");
                        String startTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "start_time", null);
                        String endTimeStr = SchemaUtil.getStringOrDefault(request.arguments(), "end_time", null);

                        String cached = service.getCachedResult(filePath, NAME, request.arguments());
                        if (cached != null) {
                            return CallToolResult.builder().addTextContent(cached).isError(false).build();
                        }

                        String result = analyze(filePath, startTimeStr, endTimeStr);
                        service.cacheResult(filePath, NAME, request.arguments(), result);
                        return CallToolResult.builder().addTextContent(result).isError(false).build();
                    } catch (Exception e) {
                        return CallToolResult.builder()
                                .addTextContent("Error: " + e.getMessage())
                                .isError(true)
                                .build();
                    }
                })
                .build();
    }

    private String analyze(String filePath, String startTimeStr, String endTimeStr) throws IOException {
        IItemCollection allEvents = service.loadRecording(filePath);
        IItemCollection events = service.filterByTimeRange(allEvents, startTimeStr, endTimeStr);

        String jvmVersion = extractJvmVersion(events);

        List<BugMatch> matches = new ArrayList<>();

        IItemCollection compilerFailures = events.apply(ItemFilters.type("jdk.CompilerFailure"));
        for (IItemIterable iterable : compilerFailures) {
            IMemberAccessor<Object, IItem> msgAccessor = JfrItemUtils.getAccessor(iterable.getType(), "failureMessage");
            IMemberAccessor<Object, IItem> methodAccessor = JfrItemUtils.getAccessor(iterable.getType(), "method");
            if (msgAccessor != null) {
                for (IItem item : iterable) {
                    Object msg = msgAccessor.getMember(item);
                    Object method = methodAccessor != null ? methodAccessor.getMember(item) : null;
                    String text = (msg != null ? msg.toString() : "") + " " + (method != null ? method.toString() : "");
                    findMatches(text, "COMPILER", jvmVersion, matches);
                }
            }
        }

        IItemCollection errorEvents = events.apply(ItemFilters.type("jdk.JavaErrorThrow"));
        for (IItemIterable iterable : errorEvents) {
            IMemberAccessor<Object, IItem> classAccessor = JfrItemUtils.getAccessor(iterable.getType(), "thrownClass");
            IMemberAccessor<Object, IItem> msgAccessor = JfrItemUtils.getAccessor(iterable.getType(), "message");
            if (classAccessor != null) {
                for (IItem item : iterable) {
                    Object thrownClass = classAccessor.getMember(item);
                    Object msg = msgAccessor != null ? msgAccessor.getMember(item) : null;
                    String className = thrownClass != null ? thrownClass.toString() : "";
                    String message = msg != null ? msg.toString() : "";
                    String text = className + " " + message;
                    String category = className.contains("InternalError") ? "COMPILER" : "MEMORY";
                    findMatches(text, category, jvmVersion, matches);
                }
            }
        }

        IItemCollection biasedRevocations = events.apply(ItemFilters.type("jdk.BiasedLockRevocation"));
        long revocationCount = JfrItemUtils.count(biasedRevocations);
        if (revocationCount > 100) {
            BugSignature biasedBug = BUG_DATABASE.stream()
                    .filter(b -> b.id.equals("JDK-8159193"))
                    .findFirst().orElse(null);
            if (biasedBug != null) {
                matches.add(new BugMatch(biasedBug, "BiasedLockRevocation count: " + revocationCount, isVersionAffected(jvmVersion, biasedBug.affectedVersions)));
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# JDK Bug Cross-Reference\n\n");

        if (jvmVersion != null) {
            sb.append("**JVM Version:** ").append(jvmVersion).append("\n\n");
        }

        if (matches.isEmpty()) {
            sb.append("## ✅ No Known JDK Bug Matches\n\n");
            sb.append("No events in this recording match known JDK bug signatures.\n\n");
            sb.append("Analyzed:\n");
            sb.append("- Compiler failures: ").append(JfrItemUtils.count(compilerFailures)).append("\n");
            sb.append("- Java errors: ").append(JfrItemUtils.count(errorEvents)).append("\n");
            sb.append("- Biased lock revocations: ").append(revocationCount).append("\n");
        } else {
            sb.append("## ⚠️ ").append(matches.size()).append(" Potential JDK Bug Match").append(matches.size() > 1 ? "es" : "").append("\n\n");

            for (BugMatch match : matches) {
                BugSignature bug = match.bug;
                sb.append("### ").append(bug.id).append("\n\n");
                sb.append("| Field | Value |\n");
                sb.append("|-------|-------|\n");
                sb.append("| Severity | ").append(bug.severity).append(" |\n");
                sb.append("| Category | ").append(bug.category).append(" |\n");
                sb.append("| Matched Pattern | `").append(match.matchedText).append("` |\n");
                sb.append("| Affected Versions | ").append(String.join(", ", bug.affectedVersions)).append(" |\n");
                if (bug.fixedIn != null) {
                    sb.append("| Fixed In | ").append(bug.fixedIn).append(" |\n");
                }
                if (match.versionAffected) {
                    sb.append("| **Your Version** | ⚠️ **AFFECTED** |\n");
                } else if (jvmVersion != null) {
                    sb.append("| **Your Version** | Likely not affected |\n");
                }
                if (bug.workaround != null) {
                    sb.append("| Workaround | `").append(bug.workaround).append("` |\n");
                }
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    private String extractJvmVersion(IItemCollection events) {
        IItemCollection propEvents = events.apply(ItemFilters.type("jdk.InitialSystemProperty"));
        for (IItemIterable iterable : propEvents) {
            IMemberAccessor<Object, IItem> keyAccessor = JfrItemUtils.getAccessor(iterable.getType(), "key");
            IMemberAccessor<Object, IItem> valueAccessor = JfrItemUtils.getAccessor(iterable.getType(), "value");
            if (keyAccessor != null && valueAccessor != null) {
                for (IItem item : iterable) {
                    Object key = keyAccessor.getMember(item);
                    if (key != null && key.toString().equals("java.version")) {
                        Object value = valueAccessor.getMember(item);
                        if (value != null) return value.toString();
                    }
                }
            }
        }

        IItemCollection osEvents = events.apply(ItemFilters.type("jdk.OSInformation"));
        for (IItemIterable iterable : osEvents) {
            IMemberAccessor<Object, IItem> versionAccessor = JfrItemUtils.getAccessor(iterable.getType(), "osVersion");
            if (versionAccessor != null) {
                for (IItem item : iterable) {
                    Object version = versionAccessor.getMember(item);
                    if (version != null) return version.toString();
                }
            }
        }
        return null;
    }

    private void findMatches(String text, String category, String jvmVersion, List<BugMatch> matches) {
        for (BugSignature bug : BUG_DATABASE) {
            if (!bug.category.equals(category) && !bug.category.equals("MEMORY") && !bug.category.equals("COMPILER")) {
                continue;
            }
            try {
                if (Pattern.compile(bug.pattern, Pattern.CASE_INSENSITIVE).matcher(text).find()) {
                    matches.add(new BugMatch(bug, text.substring(0, Math.min(100, text.length())), isVersionAffected(jvmVersion, bug.affectedVersions)));
                }
            } catch (Exception ignored) {}
        }
    }

    private boolean isVersionAffected(String jvmVersion, List<String> affectedVersions) {
        if (jvmVersion == null) return false;
        for (String affected : affectedVersions) {
            if (jvmVersion.startsWith(affected)) return true;
        }
        return false;
    }

    private record BugSignature(String id, String pattern, List<String> affectedVersions,
                                String fixedIn, String workaround, String severity, String category) {}

    private record BugMatch(BugSignature bug, String matchedText, boolean versionAffected) {}
}