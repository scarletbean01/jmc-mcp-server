package io.github.deplague.jmcmcp.tools;

import io.modelcontextprotocol.spec.McpSchema;

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

    public static Map<String, Object> jfrFileProp() {
        return stringProp("Absolute or relative path to the .jfr recording file");
    }

    public static Map<String, Object> startTimeProp() {
        return stringProp("Optional start time in ISO-8601 format (e.g., 2023-10-27T10:00:00Z)");
    }

    public static Map<String, Object> endTimeProp() {
        return stringProp("Optional end time in ISO-8601 format (e.g., 2023-10-27T10:05:00Z)");
    }

    public static Map<String, Object> commonJfrProps() {
        return props(
                "jfr_file_path", jfrFileProp(),
                "start_time", startTimeProp(),
                "end_time", endTimeProp()
        );
    }

    public static List<String> required(String... fields) {
        return List.of(fields);
    }

    public static String getString(Map<String, Object> args, String key) {
        Object val = args.get(key);
        if (val == null) {
            throw new IllegalArgumentException("Missing required argument: " + key);
        }
        return val.toString();
    }

    public static String getStringOrDefault(Map<String, Object> args, String key, String defaultValue) {
        Object val = args.get(key);
        return val != null ? val.toString() : defaultValue;
    }

    public static int getIntOrDefault(Map<String, Object> args, String key, int defaultValue) {
        Object val = args.get(key);
        if (val instanceof Number n) {
            return n.intValue();
        }
        if (val instanceof String s) {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static long getLongOrDefault(Map<String, Object> args, String key, long defaultValue) {
        Object val = args.get(key);
        if (val instanceof Number n) {
            return n.longValue();
        }
        if (val instanceof String s) {
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
}
