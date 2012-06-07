package ixcode.platform.serialise;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ObjectSerialiserTest {

    @Test
    public void serialize_to_byte_array_and_back() {

        ObjectSerialiser objectSerializer = new ObjectSerialiser();


        SimpleObject in = new SimpleObject(10, "foobar");

        byte[] bytes = objectSerializer.toByteArray(in);

        SimpleObject out = objectSerializer.fromByteArray(bytes);

        assertThat(out.id, is(10));
        assertThat(out.name, is("foobar"));


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