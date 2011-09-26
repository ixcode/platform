package ixcode.platform.text;

import org.junit.*;

import java.text.*;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class UtcDateFormatTest {

    private TimeZone systemTimeZone;

    @Before
    public void remember_system_time_zone() {
        systemTimeZone = TimeZone.getDefault();
    }

    @After
    public void reset_system_time_zone() {
        TimeZone.setDefault(systemTimeZone);
    }

    @Test
    public void provides_utc_offset_output() throws Exception {
        TimeZone istTimeZone = TimeZone.getTimeZone("IST");
        TimeZone.setDefault(istTimeZone);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date someDate = simpleDateFormat.parse("2011-01-23 06:30:45");

        String result = new UtcDateFormat().format(someDate);

        assertThat(result, is("2011-01-23T01:00:45+0000"));
    }

    @Test
    public void parses_back_into_appropriate_timezone() {
        Date parsedDate = new UtcDateFormat().parseString("2011-01-23T06:30:36+0530");

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss Z z");

        String result = simpleDateFormat.format(parsedDate);
        assertThat(result, is("2011-01-23 01:00:36 +0000 UTC"));
    }
}