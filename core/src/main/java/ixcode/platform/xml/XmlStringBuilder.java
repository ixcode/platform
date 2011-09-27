package ixcode.platform.xml;

import java.util.*;

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
        addNodeRequest(nodeName, true);
    }

    public XmlStringBuilder node(String nodeName) {
        processPendingNodeRequests();
        addNodeRequest(nodeName, false);
        return this;
    }

    public XmlStringBuilder attribute(String name, String value) {
        currentNodeRequest.addAttribute(name, value);
        return this;
    }

    public void closeContainerNode(String nodeName) {
        outdent();
        appendIndentedText(format("</%s>", nodeName));
    }


    public void closeNode(String nodeName) {
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

    private void appendIndentedText(String text) {
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

        appendIndentedText(format("<%s", currentNodeRequest.nodeName));

        for (NodeRequest.Attribute attribute : currentNodeRequest.attributes) {
            sb.append(format(" %s=\"%s\"", attribute.name, attribute.value));
        }
        sb.append(">");

        if (currentNodeRequest.indent) {
            indent();
        }
        currentNodeRequest = null;
    }

    private static class NodeRequest {
        public final String nodeName;
        public final List<Attribute> attributes = new ArrayList<Attribute>();

        private boolean indent;

        public NodeRequest(String nodeName, boolean indent) {
            this.nodeName = nodeName;
            this.indent = indent;
        }

        public void addAttribute(String name, String value) {
            attributes.add(new Attribute(name, value));
        }

        public static class Attribute {
            public final String name;
            public final String value;

            private Attribute(String name, String value) {
                this.name = name;
                this.value = value;
            }
        }
    }


}