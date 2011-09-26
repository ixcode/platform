package ixcode.platform.http.atom;

import ixcode.platform.http.representation.*;
import ixcode.platform.xml.*;

import java.net.*;
import java.util.*;

public class XmlRepresentationSerialiser extends XmlSerialiser {


    public XmlRepresentationSerialiser(int currentIndent) {

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
        Set<String> relations = representation.getSupportedRelations();

        for (String relation : relations) {
            Hyperlink hyperlink = representation.getRelationHyperlink(relation);
        }
    }
}