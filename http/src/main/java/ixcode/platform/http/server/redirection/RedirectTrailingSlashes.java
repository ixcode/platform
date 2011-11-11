package ixcode.platform.http.server.redirection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.lang.String.format;

public class RedirectTrailingSlashes implements Redirection {
    @Override public boolean handles(String path) {
        return (path.length() > 1 && path.endsWith("/"));
    }

    @Override public void redirect(String path, HttpServletRequest request, HttpServletResponse response) {
        String redirectedPath = path.substring(0, path.length()-1);
        try {
            response.sendRedirect(redirectedPath);
        } catch (IOException e) {
            throw new RuntimeException(format("Could not redirect path [%s] to [%s] (See cause)", path, redirectedPath), e);
        }
    }
}