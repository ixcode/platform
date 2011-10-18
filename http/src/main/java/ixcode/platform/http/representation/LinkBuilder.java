package ixcode.platform.http.representation;

import java.net.URI;

public class LinkBuilder<T extends LinkCollection> {
    private T parent;
    private URI uri;
    private String relation;
    private String title;

    public LinkBuilder(T parent, URI uri) {
        this.parent = parent;
        this.uri = uri;
    }

    public T as(String relation) {
        ((LinkCollection)parent).addHyperlink(uri, relation, title);
        return parent;
    }

    public LinkBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

}