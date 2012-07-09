package ixcode.platform.reflect;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.notNull;

public class ObjectFactoryTest {

    @Test
    public void can_instantiate_a_class_with_no_constructor() {
        ClassWithDefaultConstructor object = new ObjectFactory<ClassWithDefaultConstructor>().instantiate(ClassWithDefaultConstructor.class);

        object.iDoSOmething();
    }

    private static class  ClassWithDefaultConstructor {

        public ClassWithDefaultConstructor() {

        }

        public void iDoSOmething() {

        }

    }


}