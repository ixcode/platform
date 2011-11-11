package ixcode.platform.http.server;

import ixcode.platform.http.server.redirection.Redirection;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class RedirectionHandler extends AbstractHandler {

    private static final Logger log = Logger.getLogger(RedirectionHandler.class);
    private List<Redirection> redirections;

    public RedirectionHandler(List<Redirection> redirections) {
        this.redirections = redirections;
    }


    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        for (Redirection redirection : this.redirections) {
            if (redirection.handles(target)) {
                redirection.redirect(target, request, response);
                break;
            }
        }
    }
}