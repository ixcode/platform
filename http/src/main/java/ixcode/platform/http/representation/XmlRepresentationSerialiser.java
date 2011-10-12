package ixcode.platform.http.representation;

import ixcode.platform.http.representation.*;
import ixcode.platform.serialise.*;
import ixcode.platform.xml.*;

import java.util.*;

import static ixcode.platform.http.protocol.UriFactory.*;

public class XmlRepresentationSerialiser extends XmlSerialiser {

    public XmlRepresentationSerialiser() {
        super();
    }

    public XmlRepresentationSerialiser(int currentIndent) {
        super(currentIndent);
    }

    @Override protected <T> String formatNodeName(T objectToSerialise) {
        if (objectToSerialise instanceof Representation) {
            Representation representation = (Representation) objectToSerialise;
            return super.formatNodeName(representation.<Object>getEntity());
        }
        return super.formatNodeName(objectToSerialise);
    }

    @Override
    protected <T> void appendObjectGuts(T source) {
        if (source instanceof Representation) {
            Representation representation = (Representation) source;
            super.appendObjectGuts(representation.<T>getEntity());
            appendLinksFrom(representation);
            return;
        }
        super.appendObjectGuts(source);
    }

    private void appendLinksFrom(Representation representation) {
        Set<String> relations = representation.getAvailableRelations();

        for (String relation : relations) {
            List<Hyperlink> hyperlinks = representation.getHyperlinksMatching(relation);
            for (Hyperlink hyperlink : hyperlinks) {
                appendLink(hyperlink, xb);
            }
        }
    }

    public static void appendLink(Hyperlink hyperlink, XmlStringBuilder xb) {
        xb.node("link")
            .attribute("title", hyperlink.title)
            .attribute("rel", hyperlink.relation)
            .attribute("href", externalFormOf(hyperlink.uri))
          .closeNode("link");
        xb.newline();
    }

    public XmlSerialiser appendHeader() {
        xb.appendText("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        xb.newline();
        return this;
    }
}