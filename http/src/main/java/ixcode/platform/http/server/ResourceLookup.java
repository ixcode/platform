package ixcode.platform.http.server;

import javax.servlet.http.*;

public interface ResourceLookup {
    Resource findTheResourceMappedToThe(HttpServletRequest httpServletRequest);
}