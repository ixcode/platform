package ixcode.platform.reflect;

import org.junit.*;

import java.lang.reflect.*;
import java.util.*;

import static ixcode.platform.collection.SetManipulation.*;
import static org.fest.assertions.Assertions.assertThat;

public class ConstructorMatrixTest {

    @Test
    public void with_min_parameters() throws Exception {

        ConstructorMatrix matrix = new ConstructorMatrix(ObjectWithAFewConstructors.class);

        Set<String> names = hashSetOf("lastName", "age");

        ParameterSet parameterSet = matrix.findMostSpecificMatchTo(names);
        Constructor constructor = parameterSet.constructor;

        assertThat(parameterSet).isNotNull();
        assertThat(constructor.getParameterTypes().length).isEqualTo(2);

        constructor.setAccessible(true);
        ObjectWithAFewConstructors objectWithAFewConstructors = (ObjectWithAFewConstructors) constructor.newInstance(new Object[]{"Barritt", 23});

        assertThat(objectWithAFewConstructors.lastName).isEqualTo("Barritt");
        assertThat(objectWithAFewConstructors.age).isEqualTo(23);

    }

    @Test
    public void with_max_parameters() {
        ConstructorMatrix matrix = new ConstructorMatrix(ObjectWithAFewConstructors.class);

        Set<String> names = hashSetOf("lastName", "age", "firstName", "placeOfBirth");

        ParameterSet parameterSet = matrix.findMostSpecificMatchTo(names);

        assertThat(parameterSet.constructor).isNotNull();
        assertThat(parameterSet.constructor.getParameterTypes().length).isEqualTo(4);

    }

    @Test(expected = NoConstructorMatchedException.class)
    public void does_not_match_if_not_enough_parameters() {
        ConstructorMatrix matrix = new ConstructorMatrix(ObjectWithAFewConstructors.class);

        Set<String> names = hashSetOf("firstName", "placeOfBirth");

        matrix.findMostSpecificMatchTo(names);
    }

    @Test
    public void with_default_constructor() {
        ConstructorMatrix matrix = new ConstructorMatrix(ObjectWithDefaultConstructor.class);

        Set<String> names = hashSetOf("lastName", "age", "firstName", "placeOfBirth");

        ParameterSet parameterSet = matrix.findMostSpecificMatchTo(names);

        assertThat(parameterSet.constructor).isNotNull();
        assertThat(parameterSet.constructor.getParameterTypes().length).isEqualTo(0);

    }

    private static class ObjectWithDefaultConstructor {

    }

    private static class ObjectWithAFewConstructors {

        private final String lastName;
        private final int age;

        private String firstName;
        private String placeOfBirth;

        private ObjectWithAFewConstructors(String lastName, int age) {
            this.lastName = lastName;
            this.age = age;
        }

        private ObjectWithAFewConstructors(String lastName, int age, String firstName) {
            this.lastName = lastName;
            this.age = age;
            this.firstName = firstName;
        }

        private ObjectWithAFewConstructors(String lastName, int age, String firstName, String placeOfBirth) {
            this.lastName = lastName;
            this.age = age;
            this.firstName = firstName;
            this.placeOfBirth = placeOfBirth;
        }
    }
}