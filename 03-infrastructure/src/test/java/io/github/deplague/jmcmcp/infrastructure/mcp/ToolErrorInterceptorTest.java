package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.github.deplague.jmcmcp.domain.exception.AnalysisFailedException;
import io.github.deplague.jmcmcp.domain.exception.RecordingNotFoundException;
import io.github.deplague.jmcmcp.infrastructure.api.metrics.AnalysisMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolResponse;
import jakarta.interceptor.InvocationContext;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ToolErrorInterceptorTest {

    private ToolErrorInterceptor interceptor;
    private MeterRegistry meterRegistry;
    private AnalysisMetrics metrics;

    static class SampleToolClass {
        @Tool(name = "sample_test_tool")
        public ToolResponse sampleToolMethod() {
            return ToolResponse.success("ok");
        }
    }

    @BeforeEach
    void setUp() {
        interceptor = new ToolErrorInterceptor();
        meterRegistry = new SimpleMeterRegistry();
        metrics = new AnalysisMetrics(meterRegistry);
        interceptor.metrics = metrics;
        interceptor.slowToolThresholdMs = 1000;
    }

    @Test
    void mapsSecurityExceptionCorrectly() throws Exception {
        InvocationContext ctx = mock(InvocationContext.class);
        Method method = SampleToolClass.class.getMethod("sampleToolMethod");
        when(ctx.getMethod()).thenReturn(method);
        when(ctx.proceed()).thenThrow(new io.github.deplague.jmcmcp.infrastructure.security.PathAccessDeniedException());

        Object result = interceptor.intercept(ctx);
        assertThat(result).isInstanceOf(ToolResponse.class);
        ToolResponse response = (ToolResponse) result;
        assertThat(response.isError()).isTrue();
        assertThat(response.content().toString())
                .contains("Access denied: path not allowed under JMC_MCP_ALLOWED_PATHS");

        Timer timer = meterRegistry.find("jmc.mcp.tool.duration")
                .tag("tool", "sample_test_tool")
                .tag("outcome", "error")
                .timer();
        assertThat(timer).isNotNull();
        assertThat(timer.count()).isEqualTo(1);
    }

    @Test
    void passesNonPathSecurityExceptionMessageThrough() throws Exception {
        InvocationContext ctx = mock(InvocationContext.class);
        Method method = SampleToolClass.class.getMethod("sampleToolMethod");
        when(ctx.getMethod()).thenReturn(method);
        when(ctx.proceed()).thenThrow(new SecurityException("JMX host 'attacker.com' is not permitted"));

        ToolResponse response = (ToolResponse) interceptor.intercept(ctx);
        assertThat(response.isError()).isTrue();
        assertThat(response.content().toString())
                .contains("Access denied: JMX host 'attacker.com' is not permitted")
                .doesNotContain("JMC_MCP_ALLOWED_PATHS")
                .doesNotContain("allowed hosts");
    }

    @Test
    void mapsRecordingNotFoundExceptionCorrectly() throws Exception {
        InvocationContext ctx = mock(InvocationContext.class);
        Method method = SampleToolClass.class.getMethod("sampleToolMethod");
        when(ctx.getMethod()).thenReturn(method);
        when(ctx.proceed()).thenThrow(new RecordingNotFoundException("foo.jfr"));

        Object result = interceptor.intercept(ctx);
        assertThat(result).isInstanceOf(ToolResponse.class);
        ToolResponse response = (ToolResponse) result;
        assertThat(response.isError()).isTrue();
        assertThat(response.content().toString()).contains("Recording not found: foo.jfr");
    }

    @Test
    void mapsIllegalArgumentExceptionCorrectly() throws Exception {
        InvocationContext ctx = mock(InvocationContext.class);
        Method method = SampleToolClass.class.getMethod("sampleToolMethod");
        when(ctx.getMethod()).thenReturn(method);
        when(ctx.proceed()).thenThrow(new IllegalArgumentException("Invalid top_n"));

        Object result = interceptor.intercept(ctx);
        assertThat(result).isInstanceOf(ToolResponse.class);
        ToolResponse response = (ToolResponse) result;
        assertThat(response.isError()).isTrue();
        assertThat(response.content().toString()).contains("Invalid argument: Invalid top_n");
    }

    @Test
    void mapsValidationExceptionCorrectly() throws Exception {
        InvocationContext ctx = mock(InvocationContext.class);
        Method method = SampleToolClass.class.getMethod("sampleToolMethod");
        when(ctx.getMethod()).thenReturn(method);
        when(ctx.proceed()).thenThrow(new ValidationException("top_n must be positive"));

        Object result = interceptor.intercept(ctx);
        assertThat(result).isInstanceOf(ToolResponse.class);
        ToolResponse response = (ToolResponse) result;
        assertThat(response.isError()).isTrue();
        assertThat(response.content().toString()).contains("Validation error: top_n must be positive");
    }

    @Test
    void mapsDomainExceptionCorrectly() throws Exception {
        InvocationContext ctx = mock(InvocationContext.class);
        Method method = SampleToolClass.class.getMethod("sampleToolMethod");
        when(ctx.getMethod()).thenReturn(method);
        when(ctx.proceed()).thenThrow(new AnalysisFailedException("Parser error"));

        Object result = interceptor.intercept(ctx);
        assertThat(result).isInstanceOf(ToolResponse.class);
        ToolResponse response = (ToolResponse) result;
        assertThat(response.isError()).isTrue();
        assertThat(response.content().toString()).contains("Parser error");
    }

    @Test
    void handlesNullMessageGracefully() throws Exception {
        InvocationContext ctx = mock(InvocationContext.class);
        Method method = SampleToolClass.class.getMethod("sampleToolMethod");
        when(ctx.getMethod()).thenReturn(method);
        when(ctx.proceed()).thenThrow(new NullPointerException());

        Object result = interceptor.intercept(ctx);
        assertThat(result).isInstanceOf(ToolResponse.class);
        ToolResponse response = (ToolResponse) result;
        assertThat(response.isError()).isTrue();
        assertThat(response.content().toString())
                .contains("Error: NullPointerException")
                .doesNotContain("Error: null");
    }

    @Test
    void passesSuccessfulResultThroughAndRecordsMetricsAndMdc() throws Exception {
        InvocationContext ctx = mock(InvocationContext.class);
        Method method = SampleToolClass.class.getMethod("sampleToolMethod");
        when(ctx.getMethod()).thenReturn(method);
        ToolResponse expected = ToolResponse.success("ok");
        when(ctx.proceed()).thenAnswer(invocation -> {
            // Verify MDC inside execution
            assertThat(MDC.get("tool")).isEqualTo("sample_test_tool");
            return expected;
        });

        Object result = interceptor.intercept(ctx);
        assertThat(result).isSameAs(expected);

        Timer timer = meterRegistry.find("jmc.mcp.tool.duration")
                .tag("tool", "sample_test_tool")
                .tag("outcome", "success")
                .timer();
        assertThat(timer).isNotNull();
        assertThat(timer.count()).isEqualTo(1);
    }
}
