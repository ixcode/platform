package ixcode.platform.http.protocol.request;

public class RequestParameter {
    public final String name;
    public final String[] parameterValues;
    public final ParameterSource source;

    public RequestParameter(String name,
                            String[] parameterValues,
                            ParameterSource source) {

        this.name = name;
        this.parameterValues = parameterValues;
        this.source = source;
    }


    public enum ParameterSource {
        body, querystring, uri;
    }

}


