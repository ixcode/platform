package ixcode.platform.reflect;

import ixcode.platform.text.format.UtcDateFormat;
import org.junit.Test;

import java.util.Date;

import static ixcode.platform.reflect.ObjectBuilder.buildA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ObjectBuilderTest {

    @Test
    public void builds_an_object_with_simple_constructor() {

        DateKindOfObject output = buildA(DateKindOfObject.class)
                .setProperty("theDate").fromString("2011-01-23T06:30:36Z")
                .setProperty("someInteger").fromString("2334")
                .setProperty("aString").fromString("foobar")
                .setProperty("anotherString").fromString("barfoo")
                .build();

        assertThat(new UtcDateFormat().format(output.theDate), is("2011-01-23T06:30:36Z"));
        assertThat(output.someInteger, is(2334));
        assertThat(output.aString, is("foobar"));
        assertThat(output.anotherString, is("barfoo"));
    }

    public static class DateKindOfObject {
        public final Date theDate;
        public final Integer someInteger;
        public final String aString;
        public final String anotherString;

        private DateKindOfObject(Date theDate, Integer someInteger, String aString, String anotherString) {
            this.theDate = theDate;
            this.someInteger = someInteger;
            this.aString = aString;
            this.anotherString = anotherString;
        }
    }
}