package ixcode.platform.serialise;

import ixcode.platform.json.JsonObject;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class JsonBuilderTest {

    @Test
    public void builds_a_json_object() {
        SimpleObject simpleObject = new SimpleObject("johnny", 56);

        JsonObject jsonRoot = new JsonBuilder().buildFrom(simpleObject);

        assertThat(jsonRoot.<String>valueOf("name"), is("johnny"));
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