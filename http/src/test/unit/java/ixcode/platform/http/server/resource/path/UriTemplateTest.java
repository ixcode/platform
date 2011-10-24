package ixcode.platform.http.server.resource.path;

import org.apache.log4j.Logger;
import org.junit.Test;

import static ixcode.platform.http.server.resource.path.UriTemplate.uriTemplateFrom;
import static java.util.regex.Pattern.compile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class UriTemplateTest {

    private static final Logger log = Logger.getLogger(UriTemplateTest.class);

    @Test
    public void simple_match() {
        UriTemplate uriTemplate = uriTemplateFrom("/some/path");

        UriTemplateMatch match = uriTemplate.match("/some/path");

        assertThat(match.level, is(2));
    }

    @Test
    public void no_match() {
        UriTemplate uriTemplate = uriTemplateFrom("/some/path");

        UriTemplateMatch match = uriTemplate.match("/some/other/path");

        assertThat(match.level, is(0));
    }

    @Test
    public void no_match_with_parameters() {
        UriTemplate uriTemplate = uriTemplateFrom("/some/{param}");

        UriTemplateMatch match = uriTemplate.match("/some/VALUE/foo");

        assertThat(match.level, is(0));
    }

    @Test
    public void matches_with_parameters() {
        UriTemplate uriTemplate = uriTemplateFrom("/some/path/{userId}");

        UriTemplateMatch match = uriTemplate.match("/some/path/PARAM_VALUE");

        assertThat(match.level, is(2));
        assertThat(match.parameters.size(), is(1));
    }

    @Test
    public void extracts_parameter_map() {
        UriTemplate uriTemplate = uriTemplateFrom("/some/path/{userId}/foo");

        UriTemplateMatch match = uriTemplate.match("/some/path/PARAM_VALUE/foo");

        assertThat(match.parameters.get("userId"), is("PARAM_VALUE"));
    }
}