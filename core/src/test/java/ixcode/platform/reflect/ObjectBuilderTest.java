package ixcode.platform.reflect;

import ixcode.platform.text.format.UtcDateFormat;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static ixcode.platform.reflect.ObjectBuilder.buildA;
import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;

public class ObjectBuilderTest {

    @Test
    public void builds_an_object_with_simple_constructor() {

        DateKindOfObject output = buildA(DateKindOfObject.class)
                .setProperty("theDate").fromString("2011-01-23T06:30:36Z")
                .setProperty("someInteger").fromString("2334")
                .setProperty("aString").fromString("foobar")
                .setProperty("anotherString").fromString("barfoo")
                .setProperty("aList").asObject(asList("a", "b", "c", "d"))
                .build();

        assertThat(new UtcDateFormat().format(output.theDate)).isEqualTo("2011-01-23T06:30:36Z");
        assertThat(output.someInteger).isEqualTo(2334);
        assertThat(output.aString).isEqualTo("foobar");
        assertThat(output.anotherString).isEqualTo("barfoo");
        assertThat(output.aList).contains("a", "b", "c", "d");
    }

    public static class DateKindOfObject {
        public final Date theDate;
        public final Integer someInteger;
        public final String aString;
        public final String anotherString;
        public final List<String> aList;

        private DateKindOfObject(Date theDate,
                                 Integer someInteger,
                                 String aString,
                                 String anotherString,
                                 List<String> aList) {

            this.theDate = theDate;
            this.someInteger = someInteger;
            this.aString = aString;
            this.anotherString = anotherString;
            this.aList = aList;
        }
    }
}