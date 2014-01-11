package ixcode.platform.logging;

import org.apache.log4j.EnhancedPatternLayout;
import org.apache.log4j.spi.LoggingEvent;

public class JodaPatternLayout extends EnhancedPatternLayout {

    @Override public String format(LoggingEvent event) {
        return super.format(event);
    }
}