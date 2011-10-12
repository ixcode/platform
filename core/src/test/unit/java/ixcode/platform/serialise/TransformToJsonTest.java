package ixcode.platform.serialise;

import ixcode.platform.json.JsonObject;
import ixcode.platform.serialise.TransformToJson;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


public class TransformToJsonTest {

    @Test
    public void builds_a_json_object() {
        SimpleObject simpleObject = new SimpleObject("johnny", 56);

        JsonObject jsonRoot = new TransformToJson().from(simpleObject);

        JsonObject jsonEntity = jsonRoot.valueOf("simpleObject");

        assertThat(jsonEntity, is(notNullValue()));
        assertThat(jsonEntity.<String>valueOf("name"), is("johnny"));
        assertThat(jsonEntity.<Integer>valueOf("age"), is(56));
        assertThat(jsonEntity.hasValue("not_serialised"), is(false));
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