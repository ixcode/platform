package ixcode.platform.json;

import ixcode.platform.collection.Action;
import ixcode.platform.collection.FArrayList;
import ixcode.platform.collection.FList;

import java.util.List;

public class JsonArray {

    private FList<Object> items;

    public JsonArray(List<Object> items) {
        this.items = new FArrayList<Object>(items);
    }

    public static boolean isJsonArray(Object source) {
        return (source != null) && JsonArray.class.isAssignableFrom(source.getClass());
    }

    public <T> T get(int index) {
        return (T) items.get(index);
    }

    public int size() {
        return items.size();
    }

    public void apply(Action<Object> action) {
        items.apply(action);
    }


}