package ixcode.platform.http.server;

import ixcode.platform.http.resource.*;

import javax.servlet.http.*;

public interface ResourceLookup {
    HttpResource findTheResourceMappedToThe(HttpServletRequest httpServletRequest);
}