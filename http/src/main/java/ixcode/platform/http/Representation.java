package ixcode.platform.http;

import java.net.*;

public class Representation implements HypermediaType {

    private final Object entity;

    public Representation(Object entity) {
        this.entity = entity;
    }

    public <E> E getEntity() {
        return (E)entity;
    }

    public URI getLinkOfType(String type) {
        return null;
    }
}