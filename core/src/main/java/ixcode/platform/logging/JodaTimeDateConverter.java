package ixcode.platform.logging;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import org.joda.time.DateTimeZone;

import java.util.List;

public class JodaTimeDateConverter extends ClassicConverter {


    long lastTimestamp = -1;
    String timestampStrCache = null;
    CachingJodaDateTimeFormatter cachingDateFormatter = null;

    public void start() {


        String datePattern = getFirstOption();
        if (datePattern == null) {
            datePattern = CoreConstants.ISO8601_PATTERN;
        }

        if (datePattern.equals(CoreConstants.ISO8601_STR)) {
            datePattern = CoreConstants.ISO8601_PATTERN;
        }

        List optionList = getOptionList();


        try {
            cachingDateFormatter = new CachingJodaDateTimeFormatter(datePattern);
            // maximumCacheValidity =
            // CachedDateFormat.getMaximumCacheValidity(pattern);
        } catch (IllegalArgumentException e) {
            addWarn("Could not instantiate joda DateFormat with pattern " + datePattern, e);
            // default to the ISO8601 format
            cachingDateFormatter = new CachingJodaDateTimeFormatter(CoreConstants.ISO8601_PATTERN);
        }

        // if the option list contains a TZ option, then set it.
        if (optionList != null && optionList.size() > 1) {
            DateTimeZone tz = DateTimeZone.forID((String) optionList.get(1));
            cachingDateFormatter.changeTimeZoneTo(tz);
        }


    }

    public String convert(ILoggingEvent le) {
        long timestamp = le.getTimeStamp();
        return cachingDateFormatter.format(timestamp);
    }
}

