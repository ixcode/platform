package ixcode.platform.xml;

import static java.lang.String.format;

public class XmlStringBuilder {
    private static final String TAB = "    ";
    private static final String NEWLINE = System.getProperty("line.separator");

    private final StringBuilder sb = new StringBuilder();
    private int indent = 0;


    public void openContainerNode(String nodeName) {
        append(format("<%s>", nodeName));
        indent();
    }

    public void closeContainerNode(String nodeName) {
        outdent();
        append(format("</%s>", nodeName));
    }

    public void openValueNode(String nodeName) {
        append(format("<%s>", nodeName));
    }

    public void closeValueNode(String nodeName) {
        appendText(format("</%s>", nodeName));
    }

    public void newline() {
        sb.append(NEWLINE);
    }

    public void appendText(String text) {
        sb.append(text);
    }

    private void append(String text) {
         sb.append(format("%s%s", createTabbedString(indent), text));
    }

    private void indent() {
        ++indent;
    }

    private void outdent() {
        --indent;
    }


    private static String createTabbedString(int indent) {
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<indent;++i) {
            sb.append(TAB);
        }
        return sb.toString();
    }

    public String toString() {
        return sb.toString();
    }


}