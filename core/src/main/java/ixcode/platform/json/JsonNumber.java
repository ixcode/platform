package ixcode.platform.json;

public class JsonNumber extends Number implements JsonValue {

    private Number value;

    public static JsonNumber jsonNumber(Number number) {
        return new JsonNumber(number);
    }

    public JsonNumber(Number value) {
        this.value = value;
    }

    @Override public int intValue() {
        return value.intValue();
    }

    @Override public long longValue() {
        return value.longValue();
    }

    @Override public float floatValue() {
        return value.floatValue();
    }

    @Override public double doubleValue() {
        return value.doubleValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonNumber that = (JsonNumber) o;

        if (value != that.value) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }



}