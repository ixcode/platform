package ixcode.platform.http.atom;

import ixcode.platform.http.representation.*;
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
            Hyperlink hyperlink = representation.getRelationHyperlink(relation);
            xb.node("link")
                .attribute("rel", hyperlink.relation)
                .attribute("href", externalFormOf(hyperlink.uri))
            . closeNode("link");
            xb.newline();
        }
    }
}