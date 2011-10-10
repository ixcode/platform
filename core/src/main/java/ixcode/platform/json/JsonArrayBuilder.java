package ixcode.platform.json;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class JsonArrayBuilder {

    List<Object> items = new ArrayList<Object>();

    public static JsonArrayBuilder jsonArrayWith() {
        return new JsonArrayBuilder();
    }

    public JsonArrayBuilder items(Object... itemsToAdd) {
        items.addAll(asList(itemsToAdd));
        return this;
    }

    public JsonArray build() {
        return new JsonArray(items);
    }
}