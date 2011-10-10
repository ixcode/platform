package ixcode.platform.json.printer;

import static java.lang.String.format;

public class PrintTargetBuilder {

    private final PrintSource source;
    private final Object objectToPrint;

    public PrintTargetBuilder(PrintSource source, Object objectToPrint) {
        this.source = source;
        this.objectToPrint = objectToPrint;
    }

    public String toString() {
        StringBuilderTarget sbt = new StringBuilderTarget();

        source.print(objectToPrint, sbt);

        return sbt.toString();
    }

    private static class StringBuilderTarget implements PrintTarget {
        private int currentIndent = 0;
        private final StringBuilder sb = new StringBuilder();

        public String toString() {
            return sb.toString();
        }

        @Override public PrintTarget print(String text) {
            sb.append(text);
            return this;
        }

        @Override public PrintTarget print(String formatString, String... parameters) {
            sb.append(format(formatString, parameters));
            return this;
        }

        @Override public PrintTarget println(String text) {
            sb.append(text).append("\n");
            return this;
        }

        @Override public PrintTarget indent() {
            currentIndent++;
            return this;
        }

        @Override public PrintTarget outdent() {
            currentIndent--;
            return this;
        }
    }
}