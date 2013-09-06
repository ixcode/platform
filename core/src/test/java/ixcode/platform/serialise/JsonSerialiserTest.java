package ixcode.platform.serialise;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;


public class JsonSerialiserTest {

    @Test
    public void serialise_simple_object() {
        SimpleObject simpleObject = new SimpleObject("foo", 34);

        String result = new JsonSerialiser().toJson(simpleObject);

        assertThat(result).isEqualTo("{ \"name\" : \"foo\", \"age\" : 34 }");
    }

    @Test
    public void serialise_an_object_with_underscores() {
        ObjectWithUnderScores objectWithUnderScores = new ObjectWithUnderScores("johnny", "barr");

        String result = new JsonSerialiser().toJson(objectWithUnderScores);

        assertThat(result).isEqualTo("{ \"first-name\" : \"johnny\", \"last-name\" : \"barr\" }");
    }

    private static class SimpleObject {

        public final String name;
        public final int age;

        public SimpleObject(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }


    private static class ObjectWithUnderScores {

        public final String first_name;
        public final String last_name;

        private ObjectWithUnderScores(String first_name, String last_name) {
            this.first_name = first_name;
            this.last_name = last_name;
        }
    }
}