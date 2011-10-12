package ixcode.platform.http.representation;

import ixcode.platform.serialise.JsonSerialiser;
import org.junit.Test;

import static ixcode.platform.http.protocol.UriFactory.uri;

public class TransformRepresentationToJsonTest {

    @Test
    public void should_serialise_links() {
        RepresentationBuilder<SimpleObject> representationBuilder = new RepresentationBuilder(new SimpleObject("foo", 34));

        Representation representation = representationBuilder
                .linkingTo(uri("http://someuri")).as("somerelation")
                .build();

        String result = new JsonSerialiser(new TransformRepresentationToJson()).toJson(representation);

        System.out.println(result);
    }

    private static class SimpleObject {
        public final String name;
        public final int age;

        private SimpleObject(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
}