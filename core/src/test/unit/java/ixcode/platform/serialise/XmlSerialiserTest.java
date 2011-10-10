package ixcode.platform.serialise;

import ixcode.platform.serialise.*;
import org.junit.*;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class XmlSerialiserTest {

    @Test
    public void can_serialise_a_simple_object() {
        SimpleObject simpleObject = new SimpleObject("Johnny Foo", 23, new Date(234));

        String xml = new XmlSerialiser().toXml(simpleObject);

        System.out.println(xml);

        assertThat(xml, is("<simpleObject>\n    <name>Johnny Foo</name>\n    <age>23</age>\n    <dateOfBirth>1970-01-01T12:00:00+0000</dateOfBirth>\n</simpleObject>"));
    }

    public static class SimpleObject {
        public final String name;
        public final int age;
        public final Date dateOfBirth;

        private SimpleObject(String name, int age, Date dateOfBirth) {
            this.name = name;
            this.age = age;
            this.dateOfBirth = dateOfBirth;
        }
    }

}