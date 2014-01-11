package ixcode.platform.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.LoggerFactory;


public class ConsoleLogging {

    private static final Level DEFAULT_LOG_LEVEL = Level.INFO;

    private static final String LOG_PATTERN = "[%t] %-5p %-25c{1} %x : %m%n";

    public static void initialise() {
        initialise(Level.INFO, LOG_PATTERN);
    }

    public static void initialise(Level level, String pattern) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

        root.setLevel(level);
        root.setAdditive(true);

        PatternLayoutEncoder layoutEncoder = new PatternLayoutEncoder();
        layoutEncoder.setPattern(pattern);
        layoutEncoder.setContext(context);
        layoutEncoder.start();

        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<ILoggingEvent>();
        consoleAppender.setName("_default");
        consoleAppender.setEncoder(layoutEncoder);
        consoleAppender.setContext(context);

        consoleAppender.start();

        root.addAppender(consoleAppender);

        StatusPrinter.printInCaseOfErrorsOrWarnings(context);
    }

}