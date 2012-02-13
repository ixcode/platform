package ixcode.platform.http.server.resource;

import ixcode.platform.http.protocol.request.Request;
import ixcode.platform.http.protocol.response.ResponseBuilder;
import ixcode.platform.http.representation.Hyperlink;
import org.junit.Test;

import static ixcode.platform.http.protocol.HttpMethod.GET;
import static ixcode.platform.http.server.resource.RouteMap.aResourceMapRootedAt;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RouteMapTest {

    @Test
    public void can_generate_a_uri() throws Exception {
        RouteMap map = aResourceMapRootedAt("http://localhost:9878")
                .thePath("/some/{name}/with/{id}").toA(new SomeResource()).supporting(GET);

        Hyperlink hyperlink = map.linkTo(SomeResource.SomeUriTemplate.class).withName("johnny").andId(3345);

        assertThat(hyperlink.uri.toURL().toExternalForm(), is("http://localhost:9878/some/johnny/with/3345"));
    }

    static class SomeResource implements Resource {
        static class SomeUriTemplate extends UriTemplateGenerator {

            private String name;
            private Integer id;

            public SomeUriTemplate(ResourceLookup source) {
                super(source, SomeResource.class, "users");
            }

            public SomeUriTemplate withName(String name) {
                this.name = name;
                return this;
            }

            public Hyperlink andId(int id) {
                this.id = id;
                return super.hyperlink();
            }
        }

        public void GET(Request request, ResponseBuilder respondWith, ResourceHyperlinkBuilder resourceHyperlinkBuilder) {

        }


        public void POST(Request request, ResponseBuilder respondWith, ResourceHyperlinkBuilder hyperlinkBuilder) {

        }
    }


}