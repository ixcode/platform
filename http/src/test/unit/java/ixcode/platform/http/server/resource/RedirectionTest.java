package ixcode.platform.http.server.resource;

import org.junit.Test;

import java.net.URI;

import static ixcode.platform.http.server.resource.Redirection.redirect;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RedirectionTest {



    @Test
    public void redirects_with_a_relative_path() {
        URI to = redirect("http://some/path", "/some/path").to("./newpath").withNoParameters();

        assertThat(to.toString(), is("http://some/newpath"));
    }

    @Test
    public void redirects_with_parameters() {
        RedirectionParameters parameters = new RedirectionParameters().add("foo", "bar");

        URI to = redirect("http://some/path", "/some/path").to("./newpath").withParameters(parameters);

        assertThat(to.toString(), is("http://some/newpath/bar"));
    }
}