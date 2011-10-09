package ixcode.platform.json;

import java.util.*;

public class JsonArray implements JsonValue {

    private List<JsonValue> jsonValues;

    public JsonArray(List<JsonValue> jsonValues) {
        this.jsonValues = jsonValues;
    }

    public <T extends JsonValue> T valueAt(int index) {
        return (T)jsonValues.get(index);
    }

    public int size() {
        return jsonValues.size();
    }
}