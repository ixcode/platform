package ixcode.platform.xml;

import static java.lang.String.*;

public class XmlStringBuilder {
    private static final String TAB = "    ";
    private static final String NEWLINE = System.getProperty("line.separator");

    private final StringBuilder sb = new StringBuilder();
    private int indent;
    private NodeRequest currentNodeRequest;

    public XmlStringBuilder() {
        this(0);
    }

    public XmlStringBuilder(int indent) {
        this.indent = indent;
    }


    public void openContainerNode(String nodeName) {
        processPendingNodeRequests();
        addNodeRequest(format("<%s>", nodeName), true);
    }

    public XmlStringBuilder openValueNode(String nodeName) {
        processPendingNodeRequests();
        addNodeRequest(format("<%s>", nodeName), false);
        return this;
    }

    public XmlStringBuilder withAttribute(String rel, String relation) {
        return null;
    }

    public void closeContainerNode(String nodeName) {
        outdent();
        append(format("</%s>", nodeName));
    }


    public void closeValueNode(String nodeName) {
        appendText(format("</%s>", nodeName));
    }

    public void newline() {
        processPendingNodeRequests();
        sb.append(NEWLINE);
    }

    public void appendText(String text) {
        processPendingNodeRequests();
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
        for (int i = 0; i < indent; ++i) {
            sb.append(TAB);
        }
        return sb.toString();
    }

    public String toString() {
        return sb.toString();
    }

    public int getCurrentIndent() {
        return indent;
    }

    private void addNodeRequest(String nodeName, boolean indent) {
        currentNodeRequest = new NodeRequest(nodeName, indent);
    }

    private void processPendingNodeRequests() {
        if (currentNodeRequest == null) {
            return;
        }
        append(currentNodeRequest.nodeName);
        if (currentNodeRequest.indent) {
            indent();
        }
        currentNodeRequest = null;
    }

    private static class NodeRequest {
        public final String nodeName;
        private boolean indent;

        public NodeRequest(String nodeName, boolean indent) {
            this.nodeName = nodeName;
            this.indent = indent;
        }
    }


}