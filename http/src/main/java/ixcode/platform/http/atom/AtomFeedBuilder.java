package ixcode.platform.http.atom;

import ixcode.platform.http.representation.*;
import ixcode.platform.xml.*;

import java.util.*;

import static ixcode.platform.http.atom.XmlRepresentationSerialiser.appendLink;

public class AtomFeedBuilder {
    List<Representation> items = new ArrayList<Representation>();


    public AtomFeedBuilder addItem(Representation item) {
        items.add(item);
        return this;
    }

    public String asXml() {
        XmlStringBuilder xb = new XmlStringBuilder();

        xb.appendText("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        xb.newline();
        xb.openContainerNode("feed");
        xb.newline();
        for (Representation item : items) {
            xb.openContainerNode("item");
            xb.newline();

            if (item.hasRelation("self")) {
                appendLink(item.getRelationHyperlinks("self").get(0), xb);
            }

            XmlRepresentationSerialiser xrs = new XmlRepresentationSerialiser(xb.getCurrentIndent());
            xb.appendText(xrs.toXml(item));
            xb.newline();

            xb.closeContainerNode("item");
            xb.newline();

        }

        xb.closeContainerNode("feed");
        xb.newline();
        return xb.toString();
    }


}