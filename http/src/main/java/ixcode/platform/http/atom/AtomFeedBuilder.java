package ixcode.platform.http.atom;

import ixcode.platform.xml.*;

import java.util.*;

public class AtomFeedBuilder {
    List<String> items = new ArrayList<String>();

    public AtomFeedBuilder addItem(String jksShop) {
        items.add(jksShop);
        return this;
    }

    public String asXml() {
        XmlStringBuilder xb = new XmlStringBuilder();

        xb.openContainerNode("feed");
        xb.newline();
        xb.openContainerNode("item");
        xb.newline();
        for (String item : items) {
            xb.appendText(item);
            xb.newline();
        }

        xb.closeContainerNode("item");
        xb.newline();
        xb.closeContainerNode("feed");
        xb.newline();
        return xb.toString();
    }
}