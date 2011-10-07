package ixcode.platform.http.protocol;

import ixcode.platform.collection.*;

import java.util.*;

public class RequestParameters  {

    private final FList<RequestParameter> parameters = new FArrayList<RequestParameter>();

    public void apply(Action<RequestParameter> action) {
        parameters.apply(action);
    }
}