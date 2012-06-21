package ixcode.platform.serialise;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TransformToMapTest {

    @Test
    @Ignore("WIP")
    public void convert_simple_object_to_map() {
        TransformToMap toMap = new TransformToMap();

        Map<String, Object> map = toMap.from(new SimpleObject(10, "johnny"));

        assertThat((String)map.get("is"), is(this.getClass().getName() + "$SimpleObject"));
        assertThat((Integer)map.get("id"), is(10));
        assertThat((String)map.get("name"), is("johnny"));

    }

    private static class SimpleObject {
        public final int id;
        public final String name;

        private SimpleObject(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}