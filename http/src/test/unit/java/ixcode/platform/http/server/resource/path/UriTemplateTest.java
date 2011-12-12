package ixcode.platform.http.server.resource.path;

import org.apache.log4j.Logger;
import org.junit.Test;

import static ixcode.platform.http.server.resource.path.UriTemplate.uriTemplateFrom;
import static ixcode.platform.http.server.resource.path.WeightCalculator.weightOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class UriTemplateTest {
    private static final Logger log = Logger.getLogger(UriTemplateTest.class);



    @Test
    public void single_match() {
        UriTemplate uriTemplate = uriTemplateFrom(null, "/some");

        UriTemplateMatch match = uriTemplate.match("/some");

        assertThat(match.level, is(weightOf().matches(1).calculate()));
    }


    @Test
    public void multiple_match() {
        UriTemplate uriTemplate = uriTemplateFrom(null, "/some/longer/path");

        UriTemplateMatch match = uriTemplate.match("/some/longer/path");

        assertThat(match.level, is(weightOf().matches(3).calculate()));
        assertThat(match.parameters.size(), is(0));
    }


    @Test
    public void matches_with_parameters() {
        UriTemplate uriTemplate = uriTemplateFrom(null, "/some/path/{userId}");

        UriTemplateMatch match = uriTemplate.match("/some/path/PARAM_VALUE");

        assertThat(match.level, is(weightOf().matches(2).params(1).calculate()));
        assertThat(match.parameters.size(), is(1));
    }

    @Test
    public void extracts_parameter_map() {
        UriTemplate uriTemplate = uriTemplateFrom(null, "/some/path/{userId}/foo");

        UriTemplateMatch match = uriTemplate.match("/some/path/PARAM_VALUE/foo");

        assertThat(match.level, is(weightOf().matches(3).params(1).calculate()));
        assertThat(match.parameters.get("userId"), is("PARAM_VALUE"));
    }

    @Test
    public void matches_willdcards_with_parameters() {
        UriTemplate uriTemplate = uriTemplateFrom(null, "/some/{parameter}/**");

        UriTemplateMatch match = uriTemplate.match("/some/foobar/subpath/andanother");

        assertThat(match.level, is(weightOf().matches(1).params(1).withWildcard().calculate()));
        assertThat(match.parameters.get("parameter"), is("foobar"));
        assertThat(match.subpath, is("subpath/andanother"));
    }

    @Test
    public void matches_willdcards_without_parameters() {
        UriTemplate uriTemplate = uriTemplateFrom(null, "/some/foobar/**");

        UriTemplateMatch match = uriTemplate.match("/some/foobar/subpath/andanother");

        assertThat(match.level, is(weightOf().matches(2).withWildcard().calculate()));
        assertThat(match.subpath, is("subpath/andanother"));
    }

    @Test
    public void matches_only_parameters_and_wildcards() {
        UriTemplate uriTemplate = uriTemplateFrom(null, "/{parameter}/**");

        UriTemplateMatch match = uriTemplate.match("/some/foobar/subpath/andanother");

        assertThat(match.level, is(weightOf().params(1).withWildcard().calculate()));
        assertThat(match.parameters.get("parameter"), is("some"));
        assertThat(match.subpath, is("foobar/subpath/andanother"));
    }

    @Test
    public void no_match() {
        UriTemplate uriTemplate = uriTemplateFrom(null, "/some/path");

        UriTemplateMatch match = uriTemplate.match("/some/other/path");

        assertThat(match.level, is(0));
    }

    @Test
    public void no_match_with_parameters() {
        UriTemplate uriTemplate = uriTemplateFrom(null, "/some/{param}");

        UriTemplateMatch match = uriTemplate.match("/some/VALUE/foo");

        assertThat(match.level, is(0));
    }


}