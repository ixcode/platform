package ixcode.platform.serialise;

import ixcode.platform.json.JsonArray;
import ixcode.platform.json.JsonObject;
import ixcode.platform.serialise.TransformToJson;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;


public class TransformToJsonTest {

    @Test
    public void builds_a_json_object() {
        SimpleObject simpleObject = new SimpleObject("johnny", 56);

        JsonObject jsonEntity = new TransformToJson().from(simpleObject);

        assertThat(jsonEntity).isNotNull();
        assertThat(jsonEntity.<String>valueOf("name")).isEqualTo("johnny");
        assertThat(jsonEntity.<Integer>valueOf("age")).isEqualTo(56);
        assertThat(jsonEntity.hasValue("not_serialised")).isEqualTo(false);
    }

    @Test
    public void can_build_a_list() {
        List<String> strings = asList("foo", "bar", "johnny");

        JsonArray jsonArray = new TransformToJson().from(strings);

        assertThat(jsonArray.<String>get(0)).isEqualTo("foo");
        assertThat(jsonArray.<String>get(1)).isEqualTo("bar");
        assertThat(jsonArray.<String>get(2)).isEqualTo("johnny");

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