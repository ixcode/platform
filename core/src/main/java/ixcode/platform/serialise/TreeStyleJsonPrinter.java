package ixcode.platform.serialise;

import ixcode.platform.collection.Action;
import ixcode.platform.json.JsonArray;
import ixcode.platform.json.JsonObject;
import ixcode.platform.json.JsonPair;

import java.util.Collection;

import static java.lang.String.format;

public class TreeStyleJsonPrinter extends AbstractJsonPrinter {

    private final JsonValueFormat jsonFormat = new JsonValueFormat();

    private PrintTarget printTarget;


    @Override
    public void print(Object source, PrintTarget printTarget) {
        this.printTarget = printTarget;
        if (isJsonArray(source)) {
            printJsonArray((JsonArray) source);
        } else if (isJsonObject(source)) {
            printJsonObject((JsonObject) source);
        }
        throw new RuntimeException(format("Could not print object of type [%s], did not recognise it.", source.getClass()));
    }

    private void printJsonArray(JsonArray source) {
        printTarget.println("[")
                   .indent();

        source.apply(new Action<Object>() {
            @Override public void to(Object item, Collection<Object> tail) {
                print(item);
                if (tail.size() > 0) {
                    printTarget.println(",");
                } else {
                    printTarget.println("");
                }
            }
        });

        printTarget.outdent()
                   .println("]");
    }

    private void printJsonObject(JsonObject source) {
        printTarget.println("{")
                   .indent();

        source.apply(new Action<JsonPair>() {
            @Override public void to(JsonPair item, Collection<JsonPair> tail) {
                if (isJsonArray(item) || isJsonObject(item)) {
                    print(item);
                } else {
                    printTarget.print("\"%s\" : %s", item.key, jsonFormat.format(item.value));
                }
            }
        });

        printTarget.outdent()
                   .println("}");

    }

    private static boolean isJsonObject(Object source) {return JsonObject.class.isAssignableFrom(source.getClass());}

    private static boolean isJsonArray(Object source) {return JsonArray.class.isAssignableFrom(source.getClass());}


}