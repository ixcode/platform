package ixcode.platform.http.resource;

import org.junit.*;

import java.net.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static ixcode.platform.http.resource.UriBuilder.uriBasedAt;

public class ResourceRepositoryTest {

    private final ResourceRepository<String> repository = new ResourceRepository<String>();

    @Test
    public void can_get_and_put_a_resource() throws URISyntaxException {
        URI uri = new URI("http://some.co.uk/some/path");

        repository.put(uri, "Hello");

        String resource = repository.get(uri);

        assertThat(resource, is("Hello"));
    }

    @Test
    public void works_with_generated_uris() {
        UriBuilder builder = uriBasedAt("http://some.endpoint");
        URI uri = builder.createUniqueUri();

        repository.put(uri, "Jobby");

        String resource = repository.get(uri);

        assertThat(resource, is("Jobby"));
    }
}