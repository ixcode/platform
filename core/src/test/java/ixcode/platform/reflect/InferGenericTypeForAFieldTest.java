package ixcode.platform.reflect;

import org.junit.*;

import java.lang.reflect.*;
import java.util.*;

import static org.fest.assertions.Assertions.assertThat;


public class InferGenericTypeForAFieldTest {


    @Test
    public void can_we_infer_the_type_directly() throws Exception {
        assertThat(IKnowMyFieldTypes.whatIsMyListMadeOf().getSimpleName()).isEqualTo("String");
    }

    /**
     * Thanks to http://stackoverflow.com/questions/1127923/specifying-generic-collection-type-param-at-runtime-java-reflection
     */
    private static class IKnowMyFieldTypes {
        public List<String> listOfStrings;

        public static Class<?> whatIsMyListMadeOf() throws NoSuchFieldException {
            Field field = IKnowMyFieldTypes.class.getDeclaredField("listOfStrings");

            ParameterizedType type = (ParameterizedType) field.getGenericType();

            System.out.println(type.getRawType());

            for (Type typeArgument : type.getActualTypeArguments()) {
                System.out.println("  " + typeArgument);
            }

            return (Class<?>) type.getActualTypeArguments()[0];
        }
    }

}