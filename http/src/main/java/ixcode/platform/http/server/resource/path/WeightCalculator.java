package ixcode.platform.http.server.resource.path;

public class WeightCalculator {

    private static int MATCH_WEIGHT = 3;
    private static int PARAM_WEIGHT = 2;
    private static int WILDCARD_WEIGHT = 1;

    private int matches = 0;
    private int params = 0;
    private boolean withWildcard = false;

    public static WeightCalculator weightOf() {
        return new WeightCalculator();
    }

    public WeightCalculator() {
    }

    public WeightCalculator matches(int matches) {
        this.matches = matches;
        return this;
    }

    public WeightCalculator params(int params) {
        this.params = params;
        return this;
    }

    public WeightCalculator withWildcard() {
        this.withWildcard = true;
        return this;
    }

    public int calculate() {
        return (matches * MATCH_WEIGHT)
                + (params * PARAM_WEIGHT)
                + ((withWildcard) ? WILDCARD_WEIGHT : 0);
    }

    public WeightCalculator hasWildcard(boolean hasWildcard) {
        this.withWildcard = hasWildcard;
        return this;
    }
}