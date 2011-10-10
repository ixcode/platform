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
        assertThat(jsonRoot.<Integer>valueOf("age"), is(56));
        assertThat(jsonRoot.hasValue("not_serialised"), is(false));
    }

    private static class SimpleObject {
        public final String name;
        public final int age;
        public final transient String not_serialised = "bar";

        private SimpleObject(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
}