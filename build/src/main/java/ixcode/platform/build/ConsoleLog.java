package ixcode.platform.build;

import static java.lang.String.format;
import static java.lang.System.out;

public class ConsoleLog implements BuildLog {
    @Override public void println(String format, Object... parameters) {
        out.println(format(format, parameters));
    }
}