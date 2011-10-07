package ixcode.platform.http.protocol;

import ixcode.platform.collection.*;

public class RequestParameterSerialiser {
    private RequestParameters requestParameters;

    public RequestParameterSerialiser(RequestParameters requestParameters) {
        this.requestParameters = requestParameters;
    }

    public String toJson() {
        requestParameters.apply(new Action<RequestParameter>() {
            @Override public void to(RequestParameter item) {

            }
        });
        return null;
    }

}