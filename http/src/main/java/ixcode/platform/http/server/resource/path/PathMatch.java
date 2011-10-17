package ixcode.platform.http.server.resource.path;

public class PathMatch {
    public final int level;

    private static final PathMatch NO_MATCH = new PathMatch(0);

    public PathMatch(int level) {
        this.level = level;
    }

    public static PathMatch noMatch() {
        return NO_MATCH;
    }
}