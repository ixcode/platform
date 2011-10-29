package ixcode.platform.http.representation;

import ixcode.platform.http.server.*;
import org.junit.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RepresentationDecoderTest {

    @Test
    public void decodes_the_links() throws Exception {

        String xml = "<someRepresentation><link title=\"foobar\" rel=\"alternate\" href=\"http://foobar.com\" /></someRepresentation>";

        RepresentationDecoder decoder = new RepresentationDecoder(SomeObject.class);


        Representation representation = decoder.representationFrom(xml, null);

        assertThat(representation.getHyperlinksMatching("alternate", "foobar").uri.toURL().toExternalForm(), is("http://foobar.com"));

    }

    private static class SomeObject {

    }
}