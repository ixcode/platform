package ixcode.platform.http.server;

import ixcode.platform.http.server.resource.RouteMap;

public interface ResourceMapConfiguration {
    void populateRouteMap(RouteMap map);
}