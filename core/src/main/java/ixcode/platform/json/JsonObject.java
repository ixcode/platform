package ixcode.platform.json;

import ixcode.platform.collection.Action;
import ixcode.platform.collection.FArrayList;
import ixcode.platform.collection.FList;

import javax.xml.transform.Source;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonObject {
    private final Map<String, Object> valueMap;
    private final FList<JsonPair> pairs;

    public static JsonObject emptyJsonObject() {
        return new JsonObject(new LinkedHashMap<String, Object>());
    }

    public JsonObject(Map<String, Object> valueMap) {
        this.valueMap = valueMap;
        this.pairs = extractPairsFrom(this.valueMap);
    }

    public static boolean isJsonObject(Object source) {
        return (source != null) && JsonObject.class.isAssignableFrom(source.getClass());
    }

    public boolean hasValue(String name) {
        return valueMap.containsKey(name);
    }

    public <T> T valueOf(String name) {
        return (T) valueMap.get(name);
    }

    public void apply(Action<JsonPair> action) {
        pairs.apply(action);
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

    private static FList<JsonPair> extractPairsFrom(Map<String, Object> valueMap) {
        FList<JsonPair> pairs = new FArrayList<JsonPair>();
        for (Map.Entry<String, Object> entry : valueMap.entrySet()) {
            pairs.add(new JsonPair(entry.getKey(), entry.getValue()));
        }
        return pairs;
    }

}