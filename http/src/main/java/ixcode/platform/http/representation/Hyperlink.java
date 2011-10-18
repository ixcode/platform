package ixcode.platform.http.representation;

import java.net.*;

public class Hyperlink {
    public final URI uri;
    public final String relation;
    public final String title;

    public static Hyperlink hyperlinkTo(URI uri) {
        return hyperlinkTo(uri, null);
    }

    public static Hyperlink hyperlinkTo(URI uri, String relation) {
        return hyperlinkTo(uri, relation, null);
    }
    public static Hyperlink hyperlinkTo(URI uri, String relation, String title) {
        return new Hyperlink(uri, relation, title);
    }

    private Hyperlink(URI uri, String relation, String title) {
        this.uri = uri;
        this.relation = relation;
        this.title = title;
    }

    public boolean hasTitle() {
        return title != null;
    }
}