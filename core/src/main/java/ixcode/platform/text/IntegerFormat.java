package ixcode.platform.text;

import static java.lang.Integer.parseInt;

public class IntegerFormat implements Format<Integer> {
    public Integer parseString(String value) {
        return parseInt(value);
    }

    public String format(Integer source) {
        return source.toString();
    }
}