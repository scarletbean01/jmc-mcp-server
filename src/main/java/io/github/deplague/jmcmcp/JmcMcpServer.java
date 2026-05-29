package io.github.deplague.jmcmcp;

import io.github.deplague.jmcmcp.infrastructure.jfr.CallTreeCache;
import io.github.deplague.jmcmcp.infrastructure.jfr.JfrRecordingCache;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * MCP Server entry point for Java Mission Control / JFR analysis.
 *
 * <p>Communicates via stdio (stdin/stdout) using the Model Context Protocol.
 * Never writes to stdout — all logging goes to stderr via logback.</p>
 *
 */
@QuarkusMain
public final class JmcMcpServer implements QuarkusApplication {

    private static final Logger LOG = LoggerFactory.getLogger(
            JmcMcpServer.class
    );

    @Inject
    JfrRecordingCache cache;


    @Inject
    CallTreeCache callTreeCache;

    public static void main(String[] args) {
        Quarkus.run(JmcMcpServer.class, args);
    }

    @Override
    public int run(String... args) throws Exception {
        LOG.info("Starting JMC MCP Server...");

        // Register shutdown hook for graceful cleanup of daemon-thread executors
        CountDownLatch shutdownLatch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    LOG.info("Shutting down JMC MCP Server...");
                    cache.shutdown();
                    callTreeCache.shutdown();
                    shutdownLatch.countDown();
                })
        );

        // Block until shutdown signal to keep Quarkus alive
        shutdownLatch.await();
        return 0;
    }
}
