package ixcode.platform.reflect;

import org.junit.*;

import java.lang.reflect.*;
import java.util.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static ixcode.platform.collection.SetManipulation.*;

public class ConstructorMatrixTest {

    @Test
    public void with_min_parameters() throws Exception {

        ConstructorMatrix matrix = new ConstructorMatrix(ObjectWithAFewConstructors.class);

        Set<String> names = hashSet("lastName", "age");

        ConstructorMatrix.ParameterSet parameterSet = matrix.findMostSpecificMatchTo(names);
        Constructor constructor = parameterSet.constructor;

        assertThat(parameterSet, is(notNullValue()));
        assertThat(constructor.getParameterTypes().length, is(2));

        constructor.setAccessible(true);
        ObjectWithAFewConstructors objectWithAFewConstructors = (ObjectWithAFewConstructors) constructor.newInstance(new Object[]{"Barritt", 23});

        assertThat(objectWithAFewConstructors.lastName, is("Barritt"));
        assertThat(objectWithAFewConstructors.age, is(23));

    }

    @Test
    public void with_max_parameters() {
        ConstructorMatrix matrix = new ConstructorMatrix(ObjectWithAFewConstructors.class);

        Set<String> names = hashSet("lastName", "age", "firstName", "placeOfBirth");

        ConstructorMatrix.ParameterSet parameterSet = matrix.findMostSpecificMatchTo(names);

        assertThat(parameterSet.constructor, is(notNullValue()));
        assertThat(parameterSet.constructor.getParameterTypes().length, is(4));

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