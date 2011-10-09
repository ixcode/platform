package ixcode.platform.json;

public class JsonNull implements JsonValue {

    public static JsonNull jsonNull() {
        return new JsonNull();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonNull jsonNull = (JsonNull) o;


        return true;
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
}