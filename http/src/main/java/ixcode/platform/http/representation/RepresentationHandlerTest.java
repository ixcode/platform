package ixcode.platform.http.representation;

import ixcode.platform.http.representation.*;
import org.junit.*;
import ixcode.platform.xml.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class RepresentationHandlerTest {

    @Test
    public void can_parse_a_representation() {
        String xml="<simpleObject><name>Johnny Foo</name><age>23</age></simpleObject>";
        RepresentationHandler handler = new RepresentationHandler(SimpleObject.class, null);

        new XmlParser().parse(xml).using(handler);

        Representation representation = handler.buildRepresentation();
        SimpleObject entity = representation.getEntity();

        assertThat(representation, is(notNullValue()));
        assertThat(entity, is(notNullValue()));
        assertThat(entity.name, is("Johnny Foo"));
        assertThat(entity.age, is(23));
    }

    private static class SimpleObject {

        public final String name;
        public final int age;

        private SimpleObject(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

}