package ixcode.platform.text.regex;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;

/**
 * Prints out the results of a regex match for debugging
 */
public class MatcherPrinter {

    private final Matcher matcher;

    public MatcherPrinter(Matcher matcher) {
        this.matcher = matcher;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Pattern      : ").append(matcher.pattern().toString()).append("\n");
        sb.append("Matches      : ").append(matcher.matches()).append("\n");
        if (!matcher.matches()) {
            return sb.toString();
        }
        sb.append("Group Count  : ").append( matcher.groupCount()).append("\n");
        for (int i = 0; i <= matcher.groupCount(); i++) {
            sb.append("Group [" + i + "]  :" + matcher.group(i)).append("\n");
        }

        return sb.toString();
    }

}