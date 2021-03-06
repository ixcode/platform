package ixcode.platform.serialise;

import ixcode.platform.collection.MapBuilder;
import ixcode.platform.serialise.*;
import org.junit.*;

import java.util.*;

import static ixcode.platform.collection.MapBuilder.linkedHashMapWith;
import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;

public class XmlSerialiserTest {

    @Test
    public void can_serialise_a_simple_object() {
        SimpleObject simpleObject = new SimpleObject("Johnny Foo", 23, new Date(234));

        String xml = new XmlSerialiser().toXml(simpleObject);

        System.out.println(xml);

        assertThat(xml).isEqualTo("<simpleObject>    <name>Johnny Foo</name>\n    <age>23</age>\n    <dateOfBirth>1970-01-01T12:00:00Z</dateOfBirth>\n</simpleObject>\n");
    }
    
    @Test
    public void can_serialize_a_list() {
        List<String> theList = asList("First", "Second", "Third");
        
        String xml = new XmlSerialiser().toXml(theList);
        
        assertThat(xml).isEqualTo("<string>First</string>\n<string>Second</string>\n<string>Third</string>\n");
    }
    
    @Test
    public void can_serialize_a_map() {
        Map<String, String> theMap = linkedHashMapWith().key("someKey").value("aValue").build();
        
        String xml = new XmlSerialiser().toXml(theMap);
        
        assertThat(xml).isEqualTo("<someKey>    <string>aValue    </string>\n</someKey>");
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