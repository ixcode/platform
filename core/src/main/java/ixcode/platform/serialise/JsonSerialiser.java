package ixcode.platform.serialise;

public class JsonSerialiser {

    private final JsonBuilder builder = new JsonBuilder();
    private final JsonPrinter printer = new FlatJsonPrinter();

    public <T> String toJson(T object) {
        return printer.print(builder.buildFrom(object)).toString();
    }

}