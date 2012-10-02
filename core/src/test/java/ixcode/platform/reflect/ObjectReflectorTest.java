package ixcode.platform.reflect;

import org.junit.Test;

import java.util.List;

import static ixcode.platform.reflect.ObjectReflector.reflect;
import static org.fest.assertions.Assertions.assertThat;

public class ObjectReflectorTest {

    @Test
    public void can_determine_the_type_of_a_collection() {
        ObjectReflector reflector = reflect(SomethingWithACollectionProperty.class);

        Class<?> type = reflector.typeOfCollectionField("someList");

        assertThat(type.getName()).isEqualTo("java.lang.String");

    }

    public static class SomethingWithACollectionProperty {

        private final List<String> someList;

        public SomethingWithACollectionProperty(List<String> someList) {
            this.someList = someList;
        }
    }
}