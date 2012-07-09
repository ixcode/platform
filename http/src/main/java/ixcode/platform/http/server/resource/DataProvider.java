package ixcode.platform.http.server.resource;

import ixcode.platform.http.protocol.request.Request;

import java.util.Map;

public interface DataProvider {
    void populateData(Request request, Map<String, Object> templateData);
}