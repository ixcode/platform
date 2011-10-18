package ixcode.platform.http.representation;

import java.util.*;

import static java.lang.String.format;

public class Representation implements HypermediaLinks {

    private final Object entity;
    private final Map<String, List<Hyperlink>> hyperlinks;

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

    public boolean hasRelation(String relation) {
        return hyperlinks.containsKey(relation);
    }

    public List<Hyperlink> getHyperlinksMatching(String relation) {
        if (!hyperlinks.containsKey(relation)) {
            throw new RuntimeException(format("Could not find a relation [%s]", relation));
        }
        return hyperlinks.get(relation);
    }

    private static Map<String, List<Hyperlink>> mapHyperLinks(List<Hyperlink> hyperlinks) {
        Map<String, List<Hyperlink>> hyperlinkMap = new HashMap<String, List<Hyperlink>>();
        for (Hyperlink hyperlink : hyperlinks) {

            if (!hyperlinkMap.containsKey(hyperlink.relation)) {
                List<Hyperlink> hyperlinksInMap = new ArrayList<Hyperlink>();
                hyperlinkMap.put(hyperlink.relation, hyperlinksInMap);
            }
            List<Hyperlink> hyperlinksInMap = hyperlinkMap.get(hyperlink.relation);
            hyperlinksInMap.add(hyperlink);
        }
        return hyperlinkMap;
    }

    public Set<String> getAvailableRelations() {
        return hyperlinks.keySet();
    }


    public Hyperlink getHyperlinksMatching(String relation, String title) {
        List<Hyperlink> hyperlinks = getHyperlinksMatching(relation);

        for (Hyperlink hyperlink : hyperlinks) {
            if (hyperlink.hasTitle() && hyperlink.title.equals(title)) {
                return hyperlink;
            }
        }

        throw new RuntimeException(format("Could not find a hyperlink for relation [%s] and with title [%s]", relation, title));
    }

    public Representation asJson() {
        return null;
    }
}