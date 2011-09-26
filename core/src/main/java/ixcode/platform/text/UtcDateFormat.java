package ixcode.platform.text;

import java.text.*;
import java.util.*;

public class UtcDateFormat implements Format<Date> {

    private final DateFormat utcDateFormat;

    public UtcDateFormat() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        TimeZone systemTimeZone = TimeZone.getDefault();
        try {
            this.utcDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
        } finally {
            TimeZone.setDefault(systemTimeZone);
        }

    }

    public Date parseString(String source) {
        try {
            return utcDateFormat.parse(source);
        } catch (ParseException e) {
            throw new RuntimeException(String.format("Could not parse [%s] using format [%s]", source, utcDateFormat.toString()), e);
        }
    }

    public String format(Date source) {
        return utcDateFormat.format(source);
    }


}