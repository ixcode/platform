package ixcode.platform.http.protocol.request;

import ixcode.platform.collection.*;
import ixcode.platform.text.format.StringPadding;

import javax.servlet.http.*;
import java.util.*;

public class RequestParameters  {

    private final FList<RequestParameter> parameters = new FArrayList<RequestParameter>();
    private final Map<String, RequestParameter> parameterMap = new LinkedHashMap<String, RequestParameter>();

    public static RequestParameters requestParameters() {
        return new RequestParameters();
    }

    public static RequestParameters requestParametersFrom(HttpServletRequest httpServletRequest) {
        Set<String> parameterNames = httpServletRequest.getParameterMap().keySet();
        RequestParameters requestParameters = new RequestParameters();
        String queryString = httpServletRequest.getQueryString();
        for (String parameterName : parameterNames) {
            requestParameters.withParameter(queryString.contains(parameterName),
                                            parameterName,
                                            httpServletRequest.getParameterValues(parameterName));
        }
        return requestParameters;
    }


    public void apply(Action<RequestParameter> action) {
        parameters.apply(action);
    }

    RequestParameters withQueryParameter(String name, String... values) {
        return withParameter(true, name, values);
    }

    private RequestParameters withParameter(boolean isQueryParameter, String parameterName, String... parameterValues) {
        RequestParameter parameter = new RequestParameter(parameterName, parameterValues, isQueryParameter);
        parameters.add(parameter);
        parameterMap.put(parameterName, parameter);
        return this;
    }


    public String[] get(String key) {
        return parameterMap.get(key).parameterValues;
    }

    public String getFirstValueOf(String key) {
        return get(key)[0];
    }
}