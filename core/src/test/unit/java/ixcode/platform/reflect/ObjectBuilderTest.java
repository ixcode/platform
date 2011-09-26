package ixcode.platform.reflect;

import org.junit.*;

public class ObjectBuilderTest {

    @Test
    public void builds_an_object_with_simple_constructor() {

        ObjectBuilder builder = new ObjectBuilder(DateKindOfObject.class);

    }

    private static class DateKindOfObject {

    }
}