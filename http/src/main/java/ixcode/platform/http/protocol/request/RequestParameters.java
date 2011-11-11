package ixcode.platform.http.protocol.request;

import ixcode.platform.collection.*;

import javax.servlet.http.*;
import java.util.*;

import static ixcode.platform.http.protocol.request.RequestParameter.ParameterSource.body;
import static ixcode.platform.http.protocol.request.RequestParameter.ParameterSource.querystring;
import static ixcode.platform.http.protocol.request.RequestParameter.ParameterSource.uri;

public class RequestParameters  {

    private final FList<RequestParameter> parameters = new FArrayList<RequestParameter>();
    private final Map<String, RequestParameter> parameterMap = new LinkedHashMap<String, RequestParameter>();


    public static RequestParameters requestParameters() {
        return new RequestParameters();
    }

    public static RequestParameters appendUriParameters(RequestParameters fromRequest, Map<String, String> fromUri) {
        RequestParameters parameters = new RequestParameters();
        parameters.addAllParameters(fromRequest.parameters);
        for (String key : fromUri.keySet()) {
            parameters.withUriParameter(key, fromUri.get(key));
        }
        return parameters;
    }


    public static RequestParameters requestParametersFrom(HttpServletRequest httpServletRequest) {
        Set<String> parameterNames = httpServletRequest.getParameterMap().keySet();
        RequestParameters requestParameters = new RequestParameters();
        String queryString = httpServletRequest.getQueryString();
        for (String parameterName : parameterNames) {
            RequestParameter.ParameterSource parameterSource = (queryString.contains(parameterName)) ? querystring : body;
            requestParameters.withParameter(parameterSource,
                                            parameterName,
                                            httpServletRequest.getParameterValues(parameterName));
        }
        return requestParameters;
    }


    public void apply(Action<RequestParameter> action) {
        parameters.apply(action);
    }

    public ParameterTypeQuery isParameter(String parameterName) {
        return new ParameterTypeQuery(this, parameterName);
    }

    RequestParameters withQueryParameter(String name, String... values) {
        return withParameter(querystring, name, values);
    }

    private RequestParameters withUriParameter(String name, String value) {
        return withParameter(uri, name, value);
    }

    private RequestParameters withParameter(RequestParameter.ParameterSource parameterSource, String parameterName, String... parameterValues) {
        RequestParameter parameter = new RequestParameter(parameterName, parameterValues, parameterSource);
        addParameter(parameter);
        return this;
    }

    private void addAllParameters(FList<RequestParameter> parameters) {
        for (RequestParameter parameter : parameters) {
            addParameter(parameter);
        }
    }

    private void addParameter(RequestParameter parameter) {
        parameters.add(parameter);
        parameterMap.put(parameter.name, parameter);
    }


    public String[] get(String key) {
        if (parameterMap.get(key) == null) {
            return new String[] {null};
        }
        return parameterMap.get(key).parameterValues;
    }

    public String getFirstValueOf(String key) {
        return get(key)[0];
    }

    public static class ParameterTypeQuery {
        private RequestParameters parent;
        private String parameterName;

        public ParameterTypeQuery(RequestParameters parent, String parameterName) {
            this.parent = parent;
            this.parameterName = parameterName;
        }

        public boolean from(RequestParameter.ParameterSource source) {
            return source.equals(parent.parameterMap.get(parameterName).source);
        }
    }
}