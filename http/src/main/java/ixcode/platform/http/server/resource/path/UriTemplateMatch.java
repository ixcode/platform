package ixcode.platform.http.server.resource.path;

import java.util.Collections;
import java.util.Map;

public class UriTemplateMatch {
    public final int level;
    public final Map<String, String> parameters;
    public final String subpath;

    private static final UriTemplateMatch NO_MATCH = new UriTemplateMatch(0, Collections.<String, String>emptyMap());


    public UriTemplateMatch(int level, Map<String, String> parameters) {
        this(level, parameters, "");
    }

    public UriTemplateMatch(int level, Map<String, String> parameters, String subpath) {
        this.level = level;
        this.parameters = parameters;
        this.subpath = subpath;
    }

    public static UriTemplateMatch noMatch() {
        return NO_MATCH;
    }

    public String toString() {
        return "Match (" + level + ", params " + parameters.size() + ", subpath " + subpath + ")";
    }
}