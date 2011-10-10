package ixcode.platform.serialise;

import ixcode.platform.json.printer.FlatJsonPrinter;
import ixcode.platform.json.printer.JsonPrinter;

public class JsonSerialiser {

    private final TransformToJson transformToJson = new TransformToJson();
    private final JsonPrinter jsonPrinter = new FlatJsonPrinter();

    public <T> String toJson(T object) {
        return jsonPrinter
                .print(transformToJson.from(object))
                .toString();
    }

}