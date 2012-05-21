package ixcode.platform.json;

import ixcode.platform.serialise.JsonSerialiser;
import ixcode.platform.serialise.TransformToJson;

import java.util.LinkedHashMap;
import java.util.Map;

public class JsonObjectBuilder {


    Map<String, Object> valueMap = new LinkedHashMap<String, Object>();

    public static JsonObjectBuilder jsonObjectWith() {
        return new JsonObjectBuilder();
    }

    public JsonObjectBuilder attribute(String key, Object value) {
        return key(key).value(value);
    }

    public JsonPairBuilder key(String key) {
        return new JsonPairBuilder(this, key);
    }

    public JsonObject build() {
        return new JsonObject(valueMap);
    }


    private void addPair(JsonPair jsonPair) {
        valueMap.put(jsonPair.key, jsonPair.value);
    }

    public String serialize() {
        return new JsonSerialiser(new TransformToJson()).toJson(this.valueMap);
    }

    public static class JsonPairBuilder {

        private JsonObjectBuilder parent;
        private String key;

        public JsonPairBuilder(JsonObjectBuilder parent, String key) {
            this.parent = parent;
            this.key = key;
        }

        public JsonObjectBuilder value(Object value) {
            parent.addPair(new JsonPair(key, value));
            return parent;
        }
    }

}