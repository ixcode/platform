package ixcode.platform.reflect;

import ixcode.platform.collection.Action;
import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import static ixcode.platform.reflect.ObjectReflector.reflect;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ObjectReflectorTest {

    @Test
    public void can_determine_the_type_of_a_collection() {
        ObjectReflector reflector = reflect(SomethingWithACollectionProperty.class);

        Class<?> type = reflector.typeOfCollectionProperty("someList");

        assertThat(type.getName(), is("java.lang.String"));

    }

    public static class SomethingWithACollectionProperty {

        private final List<String> someList;

        public SomethingWithACollectionProperty(List<String> someList) {
            this.someList = someList;
        }
    }
}