package ixcode.platform.http.representation;

import java.net.*;
import java.util.*;

import static ixcode.platform.http.representation.Hyperlink.hyperlinkTo;

public class RepresentationBuilder<E> {
    private E entity;
    private List<Hyperlink> hyperlinks = new ArrayList<Hyperlink>();

    public static RepresentationBuilder<?> aRepresentationOf(Object entity) {
        return new RepresentationBuilder(entity);
    }

    public RepresentationBuilder(E entity) {
        this.entity = entity;
    }

    private void addHyperlink(URI uri, String relation, String title) {
        this.hyperlinks.add(hyperlinkTo(uri, relation, title));
    }

    public Representation build() {
        return new Representation(entity, new HashMap<String, List<String>>(), hyperlinks);
    }

    public LinkBuilder linkingTo(URI uri) {
        return new LinkBuilder(this, uri);
    }


    public static class LinkBuilder {
        private RepresentationBuilder<?> parent;
        private URI uri;
        private String relation;
        private String title;

        public LinkBuilder(RepresentationBuilder<?> parent, URI uri) {
            this.parent = parent;
            this.uri = uri;
        }

        public RepresentationBuilder<?>  as(String relation) {
            parent.addHyperlink(uri, relation, title);
            return parent;
        }

        public  LinkBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

    }


}