package ixcode.platform.json;

import java.util.*;

public class JsonArray  {

    private List<Object> items;

    public JsonArray(List<Object> items) {
        this.items = items;
    }

    public <T> T valueAt(int index) {
        return (T) items.get(index);
    }

    public int size() {
        return items.size();
    }
}