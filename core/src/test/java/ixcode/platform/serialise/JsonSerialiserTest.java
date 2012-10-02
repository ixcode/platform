package ixcode.platform.serialise;

import org.junit.*;

import static org.fest.assertions.Assertions.assertThat;


public class JsonSerialiserTest {

    @Test
    public void serialise_simple_object() {
        SimpleObject simpleObject = new SimpleObject("foo", 34);

        String result = new JsonSerialiser().toJson(simpleObject);

        assertThat(result).isEqualTo("{ \"name\" : \"foo\", \"age\" : 34 }");
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