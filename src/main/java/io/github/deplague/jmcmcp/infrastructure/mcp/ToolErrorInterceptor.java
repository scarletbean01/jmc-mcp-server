package io.github.deplague.jmcmcp.infrastructure.mcp;

import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.File;
import java.lang.reflect.Parameter;

@HandleToolError
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class ToolErrorInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(ToolErrorInterceptor.class);

    @AroundInvoke
    public Object intercept(InvocationContext context) throws Exception {
        String toolName = context.getMethod().getName();
        String jfrFilePath = extractJfrFilePath(context);
        String fileName = jfrFilePath != null ? new File(jfrFilePath).getName() : "no-file";

        try (MDC.MDCCloseable mdcTool = MDC.putCloseable("tool", toolName);
             MDC.MDCCloseable mdcFile = MDC.putCloseable("file", fileName)) {
            
            try {
                return context.proceed();
            } catch (Exception e) {
                LOG.warn("Tool execution failed: {}", e.getMessage(), e);
                return ToolResponse.error("Error: " + e.getMessage());
            }
        }
    }

    private String extractJfrFilePath(InvocationContext context) {
        Object[] params = context.getParameters();
        Parameter[] methodParams = context.getMethod().getParameters();
        if (params == null || methodParams == null) return null;

        for (int i = 0; i < methodParams.length; i++) {
            ToolArg arg = methodParams[i].getAnnotation(ToolArg.class);
            if (arg != null && "jfr_file_path".equals(arg.name()) && i < params.length && params[i] instanceof String s) {
                return s;
            }
        }
        return null;
    }
}
