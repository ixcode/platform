package ixcode.platform.http.representation;

import org.junit.*;

import static ixcode.platform.http.protocol.UriFactory.uri;
import static ixcode.platform.http.representation.RepresentationBuilder.aRepresentationOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class RepresentationBuilderTest {

    @Test
    public void can_build_a_representation_with_links() throws Exception {
        SomeEntity anEntity = new SomeEntity();
        Representation representation = aRepresentationOf(anEntity)
                                            .linkingTo(uri("http://bar.com")).withTitle("blob").as("alternate")
                                            .linkingTo(uri("http://foo.com")).as("self").build();

        assertThat(representation, is(notNullValue()));
        assertThat(representation.<SomeEntity>getEntity(), is(anEntity));
        assertThat(representation.getHyperlinksMatching("self").get(0).uri.toURL().toExternalForm(), is("http://foo.com"));
        assertThat(representation.getHyperlinksMatching("alternate", "blob").uri.toURL().toExternalForm(), is("http://bar.com"));
    }

    private static class SomeEntity {

    }
}