package ixcode.platform.text.format;

public class LongFormat extends AbstractFormat {

    @Override public Long parseString(String source) {
        return new Long(source);
    }

    @Override public String format(Object source) {
        return ((Long)source).toString();
    }
}