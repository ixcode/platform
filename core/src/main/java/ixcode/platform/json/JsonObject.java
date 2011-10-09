package ixcode.platform.json;

import java.util.*;

public class JsonObject implements JsonValue {
    private Map<String, JsonValue> valueMap;

    public JsonObject(Map<String, JsonValue> valueMap) {
        this.valueMap = valueMap;
    }

    public <T> T valueOf(String name) {
        return (T) valueMap.get(name);
    }
}