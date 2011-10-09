package ixcode.platform.json;

public class JsonString implements JsonValue {
    public final String value;

    public JsonString(String value) {
        this.value = value;
    }
}