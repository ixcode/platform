package ixcode.platform.reflect;

import org.junit.*;
import sun.reflect.generics.reflectiveObjects.*;

import java.lang.reflect.*;


public class InferGenericTypeForAStaticMethodTest {

    @Test
    public void can_discover_the_type_of_a_method() throws Exception {

        Class<String> type = tellMeTheTypeOfThisThing();


    }


    public static <T> Class<T> tellMeTheTypeOfThisThing() throws NoSuchMethodException {
        Method method = InferGenericTypeForAStaticMethodTest.class.getDeclaredMethod("tellMeTheTypeOfThisThing", null);

        ParameterizedType type = (ParameterizedType) method.getGenericReturnType();
        TypeVariableImpl typeImpl = (TypeVariableImpl) type.getActualTypeArguments()[0];
        System.out.println(typeImpl);
        return null;
    }

}