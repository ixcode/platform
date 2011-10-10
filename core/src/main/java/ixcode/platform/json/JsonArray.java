package ixcode.platform.json;

import java.util.*;

public class JsonArray  {

    private List<Object> jsonValues;

    public JsonArray(List<Object> jsonValues) {
        this.jsonValues = jsonValues;
    }

    public <T> T valueAt(int index) {
        return (T)jsonValues.get(index);
    }

    public int size() {
        return jsonValues.size();
    }
}