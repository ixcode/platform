package ixcode.platform.build;

import static java.lang.String.format;
import static java.lang.System.out;

public class ConsoleLog implements BuildLog {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    @Override public void printTitle(String format, Object... parameters) {
        out.print(ANSI_GREEN);
        println(format, parameters);
    }

    @Override public void println(String format, Object... parameters) {
        out.println(format(format, parameters));
    }


}