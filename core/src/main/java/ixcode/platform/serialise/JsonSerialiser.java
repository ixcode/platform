package ixcode.platform.serialise;

import ixcode.platform.json.printer.FlatJsonPrinter;
import ixcode.platform.json.printer.JsonPrinter;

public class JsonSerialiser {

    private final TransformToJson transformToJson;
    private final JsonPrinter jsonPrinter = new FlatJsonPrinter();

    public JsonSerialiser() {
        this(new TransformToJson());
    }

    public JsonSerialiser(TransformToJson transformToJson) {
        this.transformToJson = transformToJson;
    }

    public <T> String toJson(T object) {
        return jsonPrinter
                .print(transformToJson.from(object))
                .toString();
    }

}