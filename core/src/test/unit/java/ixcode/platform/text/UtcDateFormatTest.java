package ixcode.platform.text;

import org.junit.*;

import java.text.*;
import java.util.*;

import static java.util.TimeZone.*;
import static ixcode.platform.text.StringPadding.padRight;

/**
 * http://en.wikipedia.org/wiki/ISO_8601 http://www.timeanddate.com/worldclock/
 */
public class UtcDateFormatTest {

    private TimeZone timeZoneBeforeTest;

    private TimeZone systemTimeZone = getDefault();
    private TimeZone istTimeZone = getTimeZone("IST");
    private TimeZone utcTimeZone = getTimeZone("UTC");
    private TimeZone gmtTimeZone = getTimeZone("GMT");

    DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private Date someDateAndTime;

    @Before
    public void setUp() throws Exception {
        someDateAndTime = simpleDateFormat.parse("2011-09-25 10:27:46");
    }

    @Before
    public void rememberTimeZone() {
        timeZoneBeforeTest = getDefault();
    }

    @After
    public void resetTimeZone() {
        TimeZone.setDefault(timeZoneBeforeTest);
    }

    @Test
    public void simple_date_format_is_initialised_to_default_timezone() throws Exception {
        DateFormat dateFormatSystem = dateFormatWithUtcOffsetAndTimeZoneDisplayed();

        TimeZone.setDefault(istTimeZone);
        DateFormat dateFormatIst = dateFormatWithUtcOffsetAndTimeZoneDisplayed();

        TimeZone.setDefault(utcTimeZone);
        DateFormat dateFormatUtc = dateFormatWithUtcOffsetAndTimeZoneDisplayed();

        TimeZone.setDefault(gmtTimeZone);
        DateFormat dateFormatGmt = dateFormatWithUtcOffsetAndTimeZoneDisplayed();

        logTimeZone(systemTimeZone);
        logTimeZone(istTimeZone);
        logTimeZone(utcTimeZone);

        logFormattedDate(utcTimeZone, dateFormatUtc, someDateAndTime);
        logFormattedDate(gmtTimeZone, dateFormatGmt, someDateAndTime);
        logFormattedDate(systemTimeZone, dateFormatSystem, someDateAndTime);
        logFormattedDate(istTimeZone, dateFormatIst, someDateAndTime);

    }


    @Test
    public void what_happens_if_you_re_parse_a_converted_time() throws Exception {
        DateFormat dateFormatSystem = dateFormatWithUtcOffsetAndTimeZoneDisplayed();

        TimeZone.setDefault(istTimeZone);
        DateFormat dateFormatIst = dateFormatWithUtcOffsetAndTimeZoneDisplayed();

        String istDateAsString = dateFormatIst.format(someDateAndTime);



    }

    private static SimpleDateFormat dateFormatWithUtcOffsetAndTimeZoneDisplayed() {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z '(UTC 'Z')'");
    }

    private static void logTimeZone(TimeZone systemTimeZone1) {
        System.out.println(systemTimeZone1.getDisplayName() + "(" + systemTimeZone1.getID() + ")");
    }

    private static void logFormattedDate(TimeZone timeZone, DateFormat dateFormatUtc, Date dateAndTime) {
        System.out.println(padRight(timeZone.getID(), 14) + ": "  + dateFormatUtc.format(dateAndTime));
    }



}