package ixcode.platform.http.server.resource.path;

import ixcode.platform.text.regex.MatcherPrinter;
import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ixcode.platform.http.server.resource.path.PathMatch.noMatch;
import static java.util.regex.Pattern.compile;

public class PathPattern {

    private static final Logger log = Logger.getLogger(PathPattern.class);

    private final Pattern regexPattern;

    public static PathPattern pathPatternFrom(String path) {
        return new PathPattern(regexPatternFrom(path));
    }

    private static Pattern regexPatternFrom(String path) {
        String pathWithParametersSubstituted = path.replaceAll("\\{\\w*\\}", "(.*)");
        return compile(pathWithParametersSubstituted);
    }

    private PathPattern(Pattern regexPattern) {
        this.regexPattern = regexPattern;
    }

    public PathMatch match(String path) {
        Matcher matcher = regexPattern.matcher(path);
        if (log.isDebugEnabled()) {
            log.debug("Matching [" + path + "] :\n" + new MatcherPrinter(matcher).toString());
        }
        if (matcher.matches()) {
            return new PathMatch(path.split("/").length -1);
        };
        return noMatch();
    }
}