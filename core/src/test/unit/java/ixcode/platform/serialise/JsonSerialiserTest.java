package ixcode.platform.serialise;

import org.junit.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class JsonSerialiserTest {

    @Test
    public void serialise_simple_object() {
        SimpleObject simpleObject = new SimpleObject("foo", 34);

        String result = new JsonSerialiser().toJson(simpleObject);

        assertThat(result, is("{ \"name\" : \"foo\", \"age\" : 34 }"));
    }


    private static class SimpleObject {

        public final String name;
        public final int age;

        public SimpleObject(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
}