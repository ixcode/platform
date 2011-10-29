package ixcode.platform.http.representation;

public class ExceptionHypermedia extends HypermediaResourceBuilder<ExceptionHypermedia> {

private ExceptionReport exception;

public static ExceptionHypermedia exceptionHypermedia(Exception e) {
    return new ExceptionHypermedia(e);
}

ExceptionHypermedia(Exception exception) {
    super("error");
    this.exception = new ExceptionReport(exception);
}
}