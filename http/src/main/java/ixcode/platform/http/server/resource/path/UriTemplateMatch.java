package ixcode.platform.http.server.resource.path;

import java.util.Collections;
import java.util.Map;

public class UriTemplateMatch {
    public final int level;
    public final Map<String, String> parameters;

    private static final UriTemplateMatch NO_MATCH = new UriTemplateMatch(0, Collections.<String, String>emptyMap());

    public UriTemplateMatch(int level, Map<String, String> parameters) {
        this.level = level;
        this.parameters = parameters;
    }

    public static UriTemplateMatch noMatch() {
        return NO_MATCH;
    }
}