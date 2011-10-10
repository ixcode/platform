package ixcode.platform.json.printer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class JsonValueFormat {
    public String format(Object value) {
        if (value == null) {
            return "null";
        }
        if (Number.class.isAssignableFrom(value.getClass())
            || Boolean.class.isAssignableFrom(value.getClass())) {

            return value.toString();
        }

        return String.format("\"%s\"", value.toString());
    }
}