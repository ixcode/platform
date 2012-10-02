package ixcode.platform.reflect;

import com.thoughtworks.paranamer.*;
import org.junit.*;

import java.lang.reflect.*;

import static ixcode.platform.collection.SetManipulation.hashSetOf;
import static ixcode.platform.collection.SetManipulation.intersectionOf;
import static org.fest.assertions.Assertions.assertThat;

public class ParameterSetTest {

    @Test
    public void matches_even_if_less_than_number_of_params() throws Exception {
        Constructor<ObjectWithAConstructor> constructor = ObjectWithAConstructor.class.getDeclaredConstructor(String.class, int.class, int.class, String.class);

        ParameterSet parameterSet = new ParameterSet(constructor, new AdaptiveParanamer());

        int numberOfMatches = parameterSet.numberOfMatchesTo(hashSetOf("name", "postcode"));
        assertThat(numberOfMatches).isEqualTo(2);
    }

    private static class ObjectWithAConstructor {

        public final String name;
        public final int age;
        public final int height;
        public final String postcode;

        private ObjectWithAConstructor(String name, int age, int height, String postcode) {

            this.name = name;
            this.age = age;
            this.height = height;
            this.postcode = postcode;
        }
    }

}