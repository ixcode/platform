package ixcode.platform.collection;

import org.junit.*;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static ixcode.platform.collection.SetManipulation.hashSetOf;
import static ixcode.platform.collection.SetManipulation.intersectionOf;
import static ixcode.platform.collection.SetManipulationTest.ObjectAge.age;

public class SetManipulationTest {

    @Test
    public void can_intersect_two_sets_with_the_same_type() {
        Set<String> countries = hashSetOf("Germany", "UK", "France", "Argentina", "Brazil");
        Set<String> countriesInEU = hashSetOf("Germany", "UK", "France");

        Set<String> intersection = intersectionOf(countries, countriesInEU);

        assertThat(intersection.size(), is(3));
    }

    @Test
    public void can_intersect_two_sets_with_different_types() {
        Set<Integer> ages = hashSetOf(5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        Set<ObjectAge> objectAges = hashSetOf(age(5), age(10), age(13));

        Set<ObjectAge> intersection = intersectionOf(ages, objectAges, new ItemMatcher<Integer, ObjectAge>(){
            public boolean matches(Integer age, ObjectAge objectAge) {
                return age == objectAge.age;
            }
        });

        assertThat(intersection.size(), is(3));
        assertThat(intersection, containsInAnyOrder(age(5), age(10), age(13)));
    }




    public static class ObjectAge {
        public final int age;

        public static ObjectAge age(int age) {
            return new ObjectAge(age);
        }

        private ObjectAge(int age) {
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ObjectAge objectAge = (ObjectAge) o;

            if (age != objectAge.age) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return age;
        }
    }

}