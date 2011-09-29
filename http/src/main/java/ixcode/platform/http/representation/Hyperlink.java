package ixcode.platform.http.representation;

import java.net.*;

public class Hyperlink {
    public final URI uri;
    public final String relation;
    public final String title;

    public Hyperlink(URI uri, String relation) {
        this(uri, relation, null);
    }

    public Hyperlink(URI uri, String relation, String title) {
        this.uri = uri;
        this.relation = relation;
        this.title = title;
    }

    public boolean hasTitle() {
        return title != null;
    }
}