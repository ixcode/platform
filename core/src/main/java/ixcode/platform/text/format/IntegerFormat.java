package ixcode.platform.text.format;

import static java.lang.Integer.parseInt;

public class IntegerFormat extends AbstractFormat {
    public Integer parseString(String value) {
        return parseInt(value);
    }

    public String format(Object source) {
        Integer integer = (Integer)source;
        return source.toString();
    }
}