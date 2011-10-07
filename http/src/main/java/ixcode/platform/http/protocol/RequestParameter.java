package ixcode.platform.http.protocol;

public class RequestParameter {
    private final String parameterName;
    private final String[] parameterValues;
    private final boolean queryParameter;

    public RequestParameter(String parameterName,
                            String[] parameterValues,
                            boolean queryParameter) {

        this.parameterName = parameterName;
        this.parameterValues = parameterValues;
        this.queryParameter = queryParameter;
    }
}