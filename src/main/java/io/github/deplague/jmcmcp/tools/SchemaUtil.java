package io.github.deplague.jmcmcp.tools;

import io.modelcontextprotocol.spec.McpSchema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper for building {@link McpSchema.JsonSchema} input schemas without external JSON libraries.
 */
public final class SchemaUtil {

    private SchemaUtil() {
    }

    public static McpSchema.JsonSchema objectSchema(Map<String, Object> properties, List<String> required) {
        return new McpSchema.JsonSchema("object", properties, required, null, null, null);
    }

    public static McpSchema.JsonSchema objectSchema(Map<String, Object> properties) {
        return objectSchema(properties, null);
    }

    public static Map<String, Object> props(Object... keyValuePairs) {
        if (keyValuePairs.length % 2 != 0) {
            throw new IllegalArgumentException("Must provide even number of arguments (key, value pairs)");
        }
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            map.put((String) keyValuePairs[i], keyValuePairs[i + 1]);
        }
        return map;
    }

    public static Map<String, Object> stringProp(String description) {
        return stringProp(description, null);
    }

    public static Map<String, Object> stringProp(String description, List<String> enumValues) {
        Map<String, Object> p = new HashMap<>();
        p.put("type", "string");
        if (description != null) {
            p.put("description", description);
        }
        if (enumValues != null) {
            p.put("enum", enumValues);
        }
        return p;
    }

    public static Map<String, Object> intProp(String description, Object defaultValue) {
        Map<String, Object> p = new HashMap<>();
        p.put("type", "integer");
        if (description != null) {
            p.put("description", description);
        }
        if (defaultValue != null) {
            p.put("default", defaultValue);
        }
        return p;
    }

    public static Map<String, Object> numberProp(String description, Object defaultValue) {
        Map<String, Object> p = new HashMap<>();
        p.put("type", "number");
        if (description != null) {
            p.put("description", description);
        }
        if (defaultValue != null) {
            p.put("default", defaultValue);
        }
        return p;
    }

    public static List<String> required(String... fields) {
        return List.of(fields);
    }
}
