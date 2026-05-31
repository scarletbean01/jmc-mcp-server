package io.github.deplague.jmcmcp.infrastructure.observability;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.sdk.testing.exporter.InMemorySpanExporter;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class OpenTelemetryTest {

    private static final Logger log = LoggerFactory.getLogger(OpenTelemetryTest.class);

    @Inject
    InMemorySpanExporter inMemorySpanExporter;

    @Inject
    Tracer tracer;

    @Test
    void testSpanCapture() {
        // Clear previous spans
        inMemorySpanExporter.reset();

        // Start a span
        Span span = tracer.spanBuilder("test-span").startSpan();
        try (var scope = span.makeCurrent()) {
            span.setAttribute("test-attribute", "test-value");
            log.info("This log message should have a traceId");
        } finally {
            span.end();
        }

        // Verify it was captured in memory
        assertThat(inMemorySpanExporter.getFinishedSpanItems())
                .hasSize(1)
                .element(0)
                .satisfies(s -> {
                    assertThat(s.getName()).isEqualTo("test-span");
                    assertThat(s.getAttributes().asMap().get(io.opentelemetry.api.common.AttributeKey.stringKey("test-attribute")))
                            .isEqualTo("test-value");
                });
    }
}
