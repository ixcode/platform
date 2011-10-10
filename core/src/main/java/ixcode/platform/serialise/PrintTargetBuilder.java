package ixcode.platform.serialise;

public class PrintTargetBuilder {

    private PrintSource source;

    public PrintTargetBuilder(PrintSource source) {
        this.source = source;
    }

    public String toString() {
        StringBuilderTarget sbt = new StringBuilderTarget();

        source.print(source, sbt);

        return sbt.toString();
    }

    private static class StringBuilderTarget implements PrintTarget {
        private final StringBuilder sb = new StringBuilder();

        public String toString() {
            return sb.toString();
        }

        @Override public PrintTarget print(String text) {
            return null;
        }

        @Override public PrintTarget print(String formatString, String... parameters) {
            return null;
        }

        @Override public PrintTarget println(String s) {
            return null;
        }

        @Override public PrintTarget indent() {
            return null;
        }

        @Override public PrintTarget outdent() {
            return null;
        }
    }
}