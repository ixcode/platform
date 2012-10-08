package ixcode.platform.text.format;


import static ixcode.platform.reflect.ObjectReflector.reflect;

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
            if (formatRegistry.hasFormatFor(targetClass)) {
                return formatRegistry.findFormatFor(targetClass)
                                     .parseString(value);
            }

            return reflect(targetClass).invokeStringConstructor(value);

        }
    }
}