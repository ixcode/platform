package ixcode.platform.logging;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.pattern.DateConverter;

public class JodaTimePatternLayout extends PatternLayout {

    public JodaTimePatternLayout() {
        defaultConverterMap.put("d", JodaTimeDateConverter.class.getName());
        defaultConverterMap.put("date", JodaTimeDateConverter.class.getName());
    }
}