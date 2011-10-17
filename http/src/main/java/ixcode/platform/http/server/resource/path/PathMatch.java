package ixcode.platform.http.server.resource.path;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PathMatch {
    public final int level;
    public final Map<String, String> parameters;

    private static final PathMatch NO_MATCH = new PathMatch(0, Collections.<String, String>emptyMap());

    public PathMatch(int level, Map<String, String> parameters) {
        this.level = level;
        this.parameters = parameters;
    }

    public static PathMatch noMatch() {
        return NO_MATCH;
    }
}