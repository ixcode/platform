package ixcode.platform.http.resource;

import ixcode.platform.http.protocol.request.*;
import ixcode.platform.http.protocol.response.*;

public interface Resource {
    public void GET(Request request, ResponseBuilder respondWith);
}