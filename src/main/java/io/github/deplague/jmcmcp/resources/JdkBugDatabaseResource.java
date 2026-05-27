package io.github.deplague.jmcmcp.resources;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import java.util.List;
import java.util.function.BiFunction;

public final class JdkBugDatabaseResource {

    public McpServerFeatures.SyncResourceSpecification spec() {
        McpSchema.Resource resourceSchema = McpSchema.Resource.builder()
            .uri("mcp-jmc://database/jdk-bugs")
            .name("JDK Bug Database")
            .description(
                "Reference list of known OpenJDK bugs affecting JFR metrics"
            )
            .mimeType("text/markdown")
            .build();

        BiFunction<
            McpSyncServerExchange,
            McpSchema.ReadResourceRequest,
            McpSchema.ReadResourceResult
        > readHandler = (exchange, request) -> {
            String uri = request.uri();

            String markdownContent = """
                # Known JDK Bugs Reference

                This is a database of known OpenJDK bugs that may appear in JFR recordings (e.g., CompilerFailures, InternalErrors).

                * **JDK-8214231:** C2 compiler crash (PhaseIdealLoop). Affected: 11, 17.0.0-17.0.2. Workaround: `-XX:CompileCommand=exclude,com/example/Class::method`
                * **JDK-8231223:** OutOfMemoryError in Direct buffer. Affected: 11, 17. Workaround: `-XX:MaxDirectMemorySize=512m`
                * **JDK-8159193:** BiasedLockRevocation overhead. Affected: 11, 17. Workaround: `-XX:-UseBiasedLocking`
                * **JDK-8245266:** G1GC assert should_not_be_here. Affected: 11, 15. Fixed in: 16+. Workaround: `-XX:+UseZGC`
                * **JDK-8268060:** InternalError sigsegv PhaseIdealLoop. Affected: 11, 17.0.0, 17.0.1. Fixed in: 17.0.2. Workaround: `-XX:TieredStopAtLevel=1`
                * **JDK-8285916:** StackOverflowError in C2 compile. Affected: 17.0.0-17.0.2. Fixed in: 17.0.3. Workaround: `-XX:CompileCommand=exclude,*::*`
                * **JDK-8297970:** ZGC heap allocation failure. Affected: 17, 19, 20. Fixed in: 21+.
                * **JDK-8277044:** OutOfMemoryError Metaspace. Affected: 11, 17. Workaround: `-XX:MaxMetaspaceSize=512m`
                * **JDK-8256829:** CompilerFailure C2 infinite loop. Affected: 11, 15, 16. Fixed in: 17+. Workaround: `-XX:TieredStopAtLevel=1`
                * **JDK-8307424:** VirtualThread pin synchronized. Affected: 21, 22.0.0. Fixed in: 22.0.1. Workaround: Replace synchronized with ReentrantLock.
                * **JDK-8313206:** VirtualThread carrier starvation. Affected: 21, 22. Workaround: Increase `-Djdk.virtualThreadScheduler.parallelism`.
                * **JDK-8267693:** G1GC humongous allocation failure. Affected: 11, 15, 16. Fixed in: 17+. Workaround: `-XX:G1HeapRegionSize=32m`
                * **JDK-8240576:** InternalError C2 node dominates. Affected: 11, 14, 15. Fixed in: 16+. Workaround: `-XX:CompileCommand=exclude,*::*`
                * **JDK-8285972:** OutOfMemoryError GC overhead. Affected: 11, 17. Workaround: Increase `-Xmx` or reduce heap pressure.
                * **JDK-8235897:** assert failed PhaseChaitin. Affected: 11, 13, 14. Fixed in: 15+. Workaround: `-XX:TieredStopAtLevel=1`
                """;

            return new McpSchema.ReadResourceResult(
                List.of(
                    new McpSchema.TextResourceContents(
                        uri,
                        "text/markdown",
                        markdownContent
                    )
                )
            );
        };

        return new McpServerFeatures.SyncResourceSpecification(
            resourceSchema,
            readHandler
        );
    }
}
