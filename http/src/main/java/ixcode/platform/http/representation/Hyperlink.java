package ixcode.platform.http.representation;

import java.net.*;

public class Hyperlink {
    public final URI uri;
    public final String relation;

    public Hyperlink(URI uri, String relation) {
        this.uri = uri;
        this.relation = relation;
    }
}