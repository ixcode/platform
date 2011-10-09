package ixcode.platform.http.protocol.request;

public class RequestParameter {
    public final String name;
    public final String[] parameterValues;
    public final boolean queryParameter;

    public RequestParameter(String name,
                            String[] parameterValues,
                            boolean queryParameter) {

        this.name = name;
        this.parameterValues = parameterValues;
        this.queryParameter = queryParameter;
    }
}