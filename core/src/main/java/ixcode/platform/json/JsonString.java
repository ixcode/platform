package ixcode.platform.json;

public class JsonString implements JsonValue {
    public final String value;

    public static JsonString jsonString(String value) {
        return new JsonString(value);
    }

    public JsonString(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonString that = (JsonString) o;

        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}