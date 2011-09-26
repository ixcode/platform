package ixcode.platform.reflect;


import ixcode.platform.text.*;

public class StringToObjectParser {
    public Parser parse(String value) {
        return new Parser(value);
    }

    public static class Parser {
        private final String value;
        private final FormatRegistry formatRegistry = new FormatRegistry();

        public Parser(String value) {
            this.value = value;
        }

        public <T> T as(Class<T> targetClass) {
            Format<T> format = formatRegistry.findFormatFor(targetClass);
            return format.parseString(value);
        }
    }
}