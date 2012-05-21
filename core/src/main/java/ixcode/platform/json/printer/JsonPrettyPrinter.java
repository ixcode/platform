package ixcode.platform.json.printer;

public class JsonPrettyPrinter {

    public static String prettyPrintJson(String input) {
        int indent = 0;
        StringBuilder out = new StringBuilder();

        appendChar(input, out);


        return out.toString();
    }

    private static void appendChar(String input, StringBuilder out) {
        appendChar(input, out, 0);
    }

    private static void appendChar(String input, StringBuilder out, int indent) {

        if (input.length() == 0) {
            return;
        }

        char c = input.charAt(0);

        int indent1 = indent;
        final int TAB_SIZE = 2;
        switch (c) {
            case '{':
            case '[':
                indent1 = indent1 += TAB_SIZE;
                break;

            case '}':
            case ']':
                indent1 = indent1 -= TAB_SIZE;
        }
        indent = indent1;

        switch (c) {
            case ',':
            case '{':
            case '[':
                out.append(c).append("\n").append(makeTab(indent));
                break;

            case '}':
            case ']':
                out.append("\n").append(makeTab(indent)).append(c);
                break;
            default:
                out.append(c);
        }


        appendChar(input.substring(1), out, indent);
    }

    private static String makeTab(int indent) {
        StringBuilder tab = new StringBuilder();
        for (int i = 0; i < indent; ++i) {
            tab.append(" ");
        }
        return tab.toString();
    }


}