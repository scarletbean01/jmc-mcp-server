package io.github.deplague.jmcmcp.infrastructure.observability;

import io.opentelemetry.sdk.testing.exporter.InMemorySpanExporter;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

/**
 * Producer for OpenTelemetry in-memory components.
 * This satisfies the requirement for an in-memory solution when no backend is available.
 */
@ApplicationScoped
public class OpenTelemetryProducer {

    private final InMemorySpanExporter inMemorySpanExporter = InMemorySpanExporter.create();

    /**
     * Produces an {@link InMemorySpanExporter} that captures spans in memory.
     * This is useful for tests or internal inspection.
     *
     * @return a singleton instance of InMemorySpanExporter
     */
    @Produces
    @Singleton
    public InMemorySpanExporter inMemorySpanExporter() {
        return inMemorySpanExporter;
    }

    /**
     * Produces a {@link SpanProcessor} that uses the in-memory exporter.
     * Quarkus OpenTelemetry will automatically discover this bean and register it.
     *
     * @return a singleton instance of SpanProcessor
     */
    @Produces
    @Singleton
    public SpanProcessor inMemorySpanProcessor() {
        return SimpleSpanProcessor.create(inMemorySpanExporter);
    }
}
