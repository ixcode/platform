package ixcode.platform.http.atom;

import ixcode.platform.http.representation.*;
import ixcode.platform.xml.*;

import java.util.*;

public class AtomFeedBuilder {
    List<Representation> items = new ArrayList<Representation>();


    public AtomFeedBuilder addItem(Representation item) {
        items.add(item);
        return this;
    }

    public String asXml() {
        XmlStringBuilder xb = new XmlStringBuilder();

        xb.openContainerNode("feed");
        xb.newline();
        xb.openContainerNode("item");
        xb.newline();
        for (Representation item : items) {
            XmlRepresentationSerialiser xrs = new XmlRepresentationSerialiser(xb.getCurrentIndent());
            xb.appendText(xrs.toXml(item));
            xb.newline();
        }

        xb.closeContainerNode("item");
        xb.newline();
        xb.closeContainerNode("feed");
        xb.newline();
        return xb.toString();
    }


}