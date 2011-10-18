package ixcode.platform.http.representation;

import java.util.List;
import java.util.Map;

public class HypermediaResource implements HypermediaLinks {

    private final String[] types;

    public HypermediaResource(String[] types, Map<String, Object> values) {
        this.types = types;

    }

    @Override
    public List<Hyperlink> getHyperlinksMatching(String type) {
        return null;
    }
}