package ixcode.platform.http.representation;

import java.net.*;
import java.util.*;

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
        this.hyperlinks.add(new Hyperlink(uri, relation, title));
    }

    public Representation build() {
        return new Representation(entity, hyperlinks);
    }

    public LinkBuilder linkingTo(URI uri) {
        return new LinkBuilder(this, uri);
    }


    public static class LinkBuilder {
        private RepresentationBuilder<?> parent;
        private URI uri;
        private String relation;

        public LinkBuilder(RepresentationBuilder<?> parent, URI uri) {
            this.parent = parent;
            this.uri = uri;
        }

        public LinkBuilder as(String relation) {
            this.relation = relation;
            return this;
        }

        public RepresentationBuilder<?>  withTitle(String title) {
            parent.addHyperlink(uri, relation, title);
            return parent;
        }

        public Representation build() {
            return parent.build();
        }

    }


}