package ixcode.platform.http.representation;

import org.xml.sax.*;
import org.xml.sax.helpers.*;
import ixcode.platform.reflect.*;

import javax.management.relation.*;
import java.net.*;
import java.util.*;

import static ixcode.platform.http.protocol.UriFactory.uri;
import static ixcode.platform.http.representation.Hyperlink.hyperlinkTo;

public class RepresentationHandler extends DefaultHandler {

    private ObjectBuilder objectBuilder;
    private StringBuilder nodeContent;
    private List<Hyperlink> hyperlinks = new ArrayList<Hyperlink>();
    private Attributes attributes;
    private Map<String, List<String>> httpHeaders;


    public RepresentationHandler(Class<?> rootEntityClass, Map<String, List<String>> httpHeaders) {
        this.httpHeaders = httpHeaders;
        this.objectBuilder = new ObjectBuilder(rootEntityClass);
    }

    public Representation buildRepresentation() {
        return new Representation(objectBuilder.build(), httpHeaders, hyperlinks);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        this.attributes = attributes;
        nodeContent = new StringBuilder();
    }

    @Override public void characters(char[] chars, int start, int length) throws SAXException {
        if (hasContent()) {
            nodeContent.append(chars, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("link".equals(qName)) {
            buildLink();
        } else if (hasContent()) {
            objectBuilder.setProperty(qName).fromString(nodeContent.toString());
        }
        nodeContent = null;
        attributes = null;
    }

    private void buildLink() {
        String href = attributes.getValue("href");
        String relation = attributes.getValue("rel");
        String title = attributes.getValue("title");

        hyperlinks.add(hyperlinkTo(uri(href), relation, title));
    }

    private boolean hasContent() {
        return (nodeContent != null);
    }


}