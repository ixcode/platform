package ixcode.platform.http.representation;

import ixcode.platform.http.protocol.response.ResponseStatus;
import ixcode.platform.http.protocol.response.ResponseStatusCodes;

import java.util.*;

import static java.lang.String.format;

public class Representation implements HypermediaLinks {

    private ResponseStatus responseStatus;
    private final Object entity;
    private final Map<String, List<Hyperlink>> hyperlinks;
    public final Map<String, List<String>> httpHeaders;
    private final List<Hyperlink> allHyperlinks;

    public Representation(ResponseStatus responseStatus, Object entity, Map<String, List<String>> httpHeaders) {
        this(responseStatus, entity, httpHeaders, new ArrayList<Hyperlink>());
    }

    public Representation(ResponseStatus responseStatus, Object entity, Map<String, List<String>> httpHeaders, List<Hyperlink> hyperlinks) {
        this.responseStatus = responseStatus;
        this.entity = entity;
        this.httpHeaders = httpHeaders;
        this.allHyperlinks = hyperlinks;
        this.hyperlinks = mapHyperLinks(hyperlinks);
    }

    public <E> E getEntity() {
        return (E) entity;
    }

    public ResponseStatus responseStatus() {
        return responseStatus;
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

    public List<Hyperlink> getAllHyperlinks() {
        return allHyperlinks;
    }
}