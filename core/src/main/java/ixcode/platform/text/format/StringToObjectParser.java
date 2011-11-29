package ixcode.platform.text.format;


public class StringToObjectParser {
    public Parser fromString(String value) {
        return new Parser(value);
    }

    public static class Parser {
        private final String value;
        private final FormatRegistry formatRegistry = new FormatRegistry();

        public Parser(String value) {
            this.value = value;
        }

        public <T> T as(Class<T> targetClass) {
            return formatRegistry.findFormatFor(targetClass).parseString(value);
        }
    }
}