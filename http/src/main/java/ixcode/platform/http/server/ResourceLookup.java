package ixcode.platform.http.server;

import ixcode.platform.http.resource.*;

import javax.servlet.http.*;

public interface ResourceLookup {
    Resource findTheResourceMappedToThe(HttpServletRequest httpServletRequest);
}