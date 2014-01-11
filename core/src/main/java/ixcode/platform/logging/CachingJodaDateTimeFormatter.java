package ixcode.platform.logging;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class CachingJodaDateTimeFormatter {

    private final String pattern;

    private DateTimeFormatter dateTimeFormatter;
    private long lastTimestamp = -1;
    private String cachedStr = null;


    public CachingJodaDateTimeFormatter(String pattern) {
        this.pattern = pattern;
        dateTimeFormatter = DateTimeFormat.forPattern(pattern);
    }

    public final String format(long now) {

        // Not sure if Joda DateTimeFormatter is threadsafe or not.

        // See also the discussion in http://jira.qos.ch/browse/LBCLASSIC-36
        // DateFormattingThreadedThroughputCalculator and SelectiveDateFormattingRunnable
        // are also note worthy

        // The now == lastTimestamp guard minimizes synchronization
        synchronized (this) {
            if (now != lastTimestamp) {
                lastTimestamp = now;
                cachedStr = dateTimeFormatter.print(new DateTime());
            }
            return cachedStr;
        }
    }

    public void changeTimeZoneTo(DateTimeZone tz) {
        dateTimeFormatter = DateTimeFormat.forPattern(pattern).withZone(tz);
    }
}

