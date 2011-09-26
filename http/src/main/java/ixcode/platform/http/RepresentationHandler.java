package ixcode.platform.http;

import org.xml.sax.*;
import org.xml.sax.helpers.*;
import ixcode.platform.reflect.*;

public class RepresentationHandler<T> extends DefaultHandler {

    private ObjectBuilder objectBuilder;
    private StringBuilder nodeContent;

    public RepresentationHandler(Class<T> rootEntityClass) {
        this.objectBuilder = new ObjectBuilder(rootEntityClass);
    }

    public Representation buildRepresentation() {
        return new Representation(objectBuilder.build());
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        nodeContent = new StringBuilder();
    }

    @Override public void characters(char[] chars, int start, int length) throws SAXException {
        if (hasContent()) {
            nodeContent.append(chars, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (hasContent()) {
            objectBuilder.setProperty(qName).fromString(nodeContent.toString());
        }
        nodeContent = null;
    }

    private boolean hasContent() {
        return (nodeContent != null);
    }


}