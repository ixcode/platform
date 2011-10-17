package ixcode.platform.http.server.resource.path;

import ixcode.platform.text.regex.MatcherPrinter;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ixcode.platform.http.server.resource.path.PathPattern.pathPatternFrom;
import static java.util.regex.Pattern.compile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PathPatternTest {

    private static final Logger log = Logger.getLogger(PathPatternTest.class);

    @Test
    public void simple_match() {
        PathPattern pathPattern = pathPatternFrom("/some/path");

        PathMatch match = pathPattern.match("/some/path");

        assertThat(match.level, is(2));
    }

    @Test
    public void no_match() {
        PathPattern pathPattern = pathPatternFrom("/some/path");

        PathMatch match = pathPattern.match("/some/other/path");

        assertThat(match.level, is(0));
    }



    @Test
    public void matchesWithParameters() {
        PathPattern pathPattern = pathPatternFrom("/some/path/{userId}");

        PathMatch match = pathPattern.match("/some/path/PARAM_VALUE");

        assertThat(match.level, is(3));
    }
}