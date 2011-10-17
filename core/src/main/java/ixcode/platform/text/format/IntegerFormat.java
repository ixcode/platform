package ixcode.platform.text.format;

import static java.lang.Integer.parseInt;

public class IntegerFormat extends AbstractFormat<Integer> {
    public Integer parseString(String value) {
        return parseInt(value);
    }

    public String format(Integer source) {
        return source.toString();
    }
}