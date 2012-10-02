package ixcode.platform.reflect;

import org.junit.*;

import java.lang.reflect.*;

import static org.fest.assertions.Assertions.assertThat;


/**
 * Thanks to http://electrotek.wordpress.com/2010/10/23/find-out-generic-type-at-runtime/
 */
public class InferGenericTypesForBaseClassesTest {


    @Test
    public void infer_the_parameterised_type() {

        ConcreteSubClass concrete = new ConcreteSubClass();

        Class<EntityClass> entityClass = concrete.tellMeMyGenericClass();

        assertThat(entityClass).isNotNull();
        assertThat(entityClass.getSimpleName()).isEqualTo("EntityClass");
    }

    @Test
    public void infer_from_second_level_inheritance() {
        MoreConcreteSubClass concrete = new MoreConcreteSubClass();

        Class<EntityClass> entityClass = concrete.tellMeMyGenericClass();

        assertThat(entityClass).isNotNull();
        assertThat(entityClass.getSimpleName()).isEqualTo("EntityClass");
    }

    /**
     * Unfortunately due to type erasure, we can't find out the generic type directly
     */
    @Test
    public void instantiate_generic_class_directly() {
        GenericBaseClass<EntityClass> concrete = new GenericBaseClass<EntityClass>(EntityClass.class);

        Class<EntityClass> entityClass = concrete.tellMeMyGenericClass();

        assertThat(entityClass).isNotNull();
        assertThat(entityClass.getSimpleName()).isEqualTo("EntityClass");
    }


    private static class GenericBaseClass<T> {

        private final Class<T> elementClass;

        public GenericBaseClass(Class<T> elementClass) {
            this.elementClass = elementClass;
        }

        protected GenericBaseClass() {
            Class<?> cl = getClass();

            if (Object.class.getSimpleName().equals(cl.getSuperclass().getSimpleName())) {
                throw new IllegalArgumentException(
                        "Default constructor does not support direct instantiation");
            }

            while (!GenericBaseClass.class.getSimpleName().equals(cl.getSuperclass().getSimpleName())) {
                // case of multiple inheritance, we are trying to get the first available generic info
                if (cl.getGenericSuperclass() instanceof ParameterizedType) {
                    break;
                }
                cl = cl.getSuperclass();
            }

            if (cl.getGenericSuperclass() instanceof ParameterizedType) {
                elementClass = (Class<T>) ((ParameterizedType) cl.getGenericSuperclass()).getActualTypeArguments()[0];
            } else {
                throw new RuntimeException("Could not infer the generic type!");
            }
        }

        public Class<T> tellMeMyGenericClass() {
            return elementClass;
        }
    }

    private static class ConcreteSubClass extends GenericBaseClass<EntityClass> {
    }

    private static class MoreConcreteSubClass extends ConcreteSubClass {

    }

    private static class EntityClass {
    }

}


