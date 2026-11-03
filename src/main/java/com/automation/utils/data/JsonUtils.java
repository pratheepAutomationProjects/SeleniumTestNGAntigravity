package com.automation.utils.data;

import com.automation.utils.logging.Log;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Jackson-based JSON reader and serializer utility.
 */
public final class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonUtils() {}

    public static Map<String, Object> readJsonAsMap(String filePath) {
        try {
            return objectMapper.readValue(new File(filePath), new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            Log.error("Failed to read JSON from " + filePath + ": " + e.getMessage(), e);
            return Collections.emptyMap();
        }
    }

    public static List<Map<String, Object>> readJsonAsListOfMaps(String filePath) {
        try {
            return objectMapper.readValue(new File(filePath), new TypeReference<List<Map<String, Object>>>() {});
        } catch (IOException e) {
            Log.error("Failed to read JSON list from " + filePath + ": " + e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public static <T> T readJsonAsObject(String filePath, Class<T> clazz) {
        try {
            return objectMapper.readValue(new File(filePath), clazz);
        } catch (IOException e) {
            Log.error("Failed to map JSON to class " + clazz.getSimpleName() + ": " + e.getMessage(), e);
            return null;
        }
    }

    public static String toJson(Object object) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception e) {
            Log.error("Failed to serialize object to JSON: " + e.getMessage(), e);
            return "{}";
        }
    }
}
