package ixcode.platform.http.protocol.request;

public class RequestParameter {
    public final String name;
    public final String[] parameterValues;
    public final ParameterSource queryParameter;

    public RequestParameter(String name,
                            String[] parameterValues,
                            ParameterSource queryParameter) {

        this.name = name;
        this.parameterValues = parameterValues;
        this.queryParameter = queryParameter;
    }


    public enum ParameterSource {
        body, query, uri;
    }

}


