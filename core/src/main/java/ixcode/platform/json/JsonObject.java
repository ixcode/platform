package ixcode.platform.json;

import java.util.*;

public class JsonObject {
    private Map<String, Object> valueMap;

    public static JsonObject emptyJsonObject() {
        return new JsonObject(new LinkedHashMap<String, Object>());
    }

    public JsonObject(Map<String, Object> valueMap) {
        this.valueMap = valueMap;
    }

    public <T> T valueOf(String name) {
        return (T) valueMap.get(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonObject that = (JsonObject) o;

        if (valueMap != null ? !valueMap.equals(that.valueMap) : that.valueMap != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return valueMap != null ? valueMap.hashCode() : 0;
    }
}