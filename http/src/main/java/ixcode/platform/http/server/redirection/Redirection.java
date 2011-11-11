package ixcode.platform.http.server.redirection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Redirection {
    boolean handles(String path);

    void redirect(String target, HttpServletRequest request, HttpServletResponse response);
}