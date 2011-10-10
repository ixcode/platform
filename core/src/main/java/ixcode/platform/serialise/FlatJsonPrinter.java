package ixcode.platform.serialise;

import ixcode.platform.collection.Action;
import ixcode.platform.json.JsonArray;
import ixcode.platform.json.JsonObject;
import ixcode.platform.json.JsonPair;

import java.util.Collection;

import static java.lang.String.format;

public class FlatJsonPrinter extends AbstractJsonPrinter {

    private final JsonValueFormat jsonFormat = new JsonValueFormat();

    private PrintTarget printTarget;


    @Override
    public void print(Object source, PrintTarget printTarget) {
        this.printTarget = printTarget;
        if (isJsonArray(source)) {
            printJsonArray((JsonArray) source);
        } else if (isJsonObject(source)) {
            printJsonObject((JsonObject) source);
        } else {
            throw new RuntimeException(format("Could not print object of type [%s], did not recognise it.", source.getClass()));
        }
    }

    private void printJsonArray(JsonArray source) {
        printTarget.print("[ ");

        source.apply(new Action<Object>() {
            @Override public void to(Object item, Collection<Object> tail) {
                print(item);
                addComma(tail);
            }
        });

        printTarget.print(" ]");
    }

    private void printJsonObject(JsonObject source) {
        printTarget.print("{ ");

        source.apply(new Action<JsonPair>() {
            @Override public void to(JsonPair item, Collection<JsonPair> tail) {
                if (isJsonArray(item) || isJsonObject(item)) {
                    print(item);
                } else {
                    printTarget.print("\"%s\" : %s", item.key, jsonFormat.format(item.value));
                }
                addComma(tail);
            }
        });

        printTarget.print(" }");

    }

    private static boolean isJsonObject(Object source) {
        return JsonObject.class.isAssignableFrom(source.getClass());
    }

    private static boolean isJsonArray(Object source) {
        return JsonArray.class.isAssignableFrom(source.getClass());
    }

    private void addComma(Collection<?> tail) {
        if (tail.size() > 0) {
            printTarget.print(", ");
        } else {
            printTarget.print("");
        }
    }

}