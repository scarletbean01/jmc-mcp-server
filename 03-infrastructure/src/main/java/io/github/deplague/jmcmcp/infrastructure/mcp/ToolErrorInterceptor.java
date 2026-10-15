package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.domain.exception.JmcMcpDomainException;
import io.github.deplague.jmcmcp.domain.exception.RecordingNotFoundException;
import io.github.deplague.jmcmcp.infrastructure.api.metrics.AnalysisMetrics;
import io.github.deplague.jmcmcp.infrastructure.security.PathAccessDeniedException;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.validation.ValidationException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.File;
import java.lang.reflect.Parameter;

/**
 * Interceptor for MCP tools providing centralized error handling,
 * metrics recording, MDC logging, and slow execution tracking.
 */
@HandleToolError
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class ToolErrorInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(ToolErrorInterceptor.class);

    @Inject
    AnalysisMetrics metrics;

    @ConfigProperty(name = "jmc.mcp.slow-tool-threshold-ms", defaultValue = "5000")
    long slowToolThresholdMs = 5000;

    @AroundInvoke
    public Object intercept(InvocationContext context) throws Exception {
        io.quarkiverse.mcp.server.Tool toolAnnotation = context.getMethod().getAnnotation(io.quarkiverse.mcp.server.Tool.class);
        String toolName = (toolAnnotation != null && !toolAnnotation.name().isBlank())
                ? toolAnnotation.name()
                : context.getMethod().getName();
        String jfrFilePath = extractJfrFilePath(context);
        String fileName = jfrFilePath != null ? new File(jfrFilePath).getName() : "no-file";

        long startTime = System.currentTimeMillis();

        try (MDC.MDCCloseable _ = MDC.putCloseable("tool", toolName);
             MDC.MDCCloseable _ = MDC.putCloseable("file", fileName)) {

            try {
                Object result = context.proceed();
                long duration = System.currentTimeMillis() - startTime;

                String outcome = "success";
                if (result instanceof ToolResponse resp && resp.isError()) {
                    outcome = "error";
                }

                recordMetrics(toolName, outcome, duration);
                checkSlowExecution(toolName, duration);

                return result;
            } catch (Exception e) {
                long duration = System.currentTimeMillis() - startTime;
                recordMetrics(toolName, "error", duration);
                recordSpanError(e);

                LOG.warn("MCP tool '{}' failed after {}ms: {}", toolName, duration, e.getMessage(), e);

                if (e instanceof PathAccessDeniedException) {
                    // Fixed message: never echo server-side paths or the allowlist to the client.
                    return ToolResponse.error(PathAccessDeniedException.MESSAGE);
                }
                if (e instanceof SecurityException) {
                    String msg = e.getMessage() != null ? e.getMessage() : "operation not permitted";
                    return ToolResponse.error(msg.startsWith("Access denied") ? msg : "Access denied: " + msg);
                }
                if (e instanceof RecordingNotFoundException) {
                    String msg = e.getMessage() != null ? e.getMessage() : "recording file not found";
                    return ToolResponse.error("Recording not found: " + msg);
                }
                if (e instanceof IllegalArgumentException) {
                    String msg = e.getMessage() != null ? e.getMessage() : "invalid argument";
                    if (msg.startsWith("Invalid argument: ")) {
                        return ToolResponse.error(msg);
                    }
                    return ToolResponse.error("Invalid argument: " + msg);
                }
                if (e instanceof ValidationException) {
                    String msg = e.getMessage() != null ? e.getMessage() : "validation failed";
                    if (msg.startsWith("Validation error: ")) {
                        return ToolResponse.error(msg);
                    }
                    return ToolResponse.error("Validation error: " + msg);
                }
                if (e instanceof JmcMcpDomainException) {
                    return ToolResponse.error(e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName());
                }
                String msg = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
                return ToolResponse.error("Error: " + msg);
            }
        }
    }

    private void recordMetrics(String toolName, String outcome, long duration) {
        if (metrics != null) {
            metrics.recordMcpToolDuration(toolName, outcome, duration);
        }
    }

    private void checkSlowExecution(String toolName, long duration) {
        if (duration > slowToolThresholdMs) {
            LOG.warn("MCP tool '{}' took {}ms (threshold: {}ms)", toolName, duration, slowToolThresholdMs);
        }
    }

    private void recordSpanError(Throwable t) {
        try {
            Span span = Span.current();
            if (span != null && span.isRecording()) {
                span.recordException(t);
                span.setStatus(StatusCode.ERROR, t.getMessage() != null ? t.getMessage() : t.getClass().getSimpleName());
            }
        } catch (Throwable ignored) {
        }
    }

    private String extractJfrFilePath(InvocationContext context) {
        Object[] params = context.getParameters();
        Parameter[] methodParams = context.getMethod().getParameters();
        if (params == null || methodParams == null) return null;

        for (int i = 0; i < methodParams.length; i++) {
            ToolArg arg = methodParams[i].getAnnotation(ToolArg.class);
            if (arg != null && i < params.length && params[i] instanceof String s) {
                if ("jfr_file_path".equals(arg.name()) || "recording_path".equals(arg.name()) || "heap_dump_path".equals(arg.name())) {
                    return s;
                }
            }
        }
        return null;
    }
}
