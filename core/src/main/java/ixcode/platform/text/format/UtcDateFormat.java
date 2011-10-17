package ixcode.platform.text.format;

import java.text.*;
import java.util.*;

import static java.util.TimeZone.getTimeZone;

public class UtcDateFormat extends AbstractFormat<Date> {

    private final DateFormat utcDateFormat;

    public UtcDateFormat() {
        TimeZone systemTimeZone = TimeZone.getDefault();
        try {
            TimeZone.setDefault(getTimeZone("UTC"));
            this.utcDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
        } finally {
            TimeZone.setDefault(systemTimeZone);
        }

    }

    public Date parseString(String source) {
        TimeZone systemTimeZone = TimeZone.getDefault();
        try {
            TimeZone.setDefault(getTimeZone("UTC"));
            return utcDateFormat.parse(source);
        } catch (ParseException e) {
            throw new RuntimeException(String.format("Could not parse [%s] using format [%s]", source, utcDateFormat.toString()), e);
        } finally {
            TimeZone.setDefault(systemTimeZone);
        }
    }

    public String format(Date source) {
        return utcDateFormat.format(source);
    }


}