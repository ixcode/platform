package ixcode.platform.http.representation;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class ExceptionReport {

public final String type;
public final String message;
private List<String> stackTrace;

public ExceptionReport(Exception exception) {
    type = exception.getClass().getName();
    message = exception.getMessage();
    stackTrace = new ArrayList<String>();
    for (StackTraceElement element : asList(exception.getStackTrace())) {
        stackTrace.add(element.toString());
    }
}
}