package ixcode.platform.http.representation;

import java.net.*;
import java.util.*;

public class Representation implements HypermediaType {

    private final Object entity;
    private final Map<String, Hyperlink> hyperlinks;

    public Representation(Object entity) {
        this(entity, new ArrayList<Hyperlink>());
    }

    public Representation(Object entity, List<Hyperlink> hyperlinks) {
        this.entity = entity;
        this.hyperlinks = mapHyperLinks(hyperlinks);
    }

    public <E> E getEntity() {
        return (E) entity;
    }

    public Hyperlink getRelationHyperlink(String relation) {
        return hyperlinks.get(relation);
    }

    private static Map<String, Hyperlink> mapHyperLinks(List<Hyperlink> hyperlinks) {
        Map<String, Hyperlink> hyperlinkMap = new HashMap<String, Hyperlink>();
        for (Hyperlink hyperlink : hyperlinks) {
            hyperlinkMap.put(hyperlink.relation, hyperlink);
        }
        return hyperlinkMap;
    }

    public Set<String> getSupportedRelations() {
        return hyperlinks.keySet();
    }
}