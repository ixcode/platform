package ixcode.platform.json;

public class JsonBoolean implements JsonValue {

    private Boolean value;

    public static JsonBoolean jsonBoolean(Boolean value) {
        return new JsonBoolean(value);
    }

    public JsonBoolean(Boolean value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonBoolean that = (JsonBoolean) o;

        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}