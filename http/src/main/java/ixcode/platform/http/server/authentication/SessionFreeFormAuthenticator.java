package ixcode.platform.http.server.authentication;

import org.apache.log4j.Logger;
import org.eclipse.jetty.security.ServerAuthException;
import org.eclipse.jetty.security.authentication.LoginAuthenticator;
import org.eclipse.jetty.server.Authentication;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static ixcode.platform.http.server.authentication.MemoryAuthenticationCache.STATIC_AUTHENTICATION_CACHE;
import static ixcode.platform.io.IoStreamHandling.closeQuietly;
import static org.eclipse.jetty.server.Authentication.SEND_FAILURE;

/**
 * The normal FormAuthenticator requires the use of the HttpSession, which is
 * not always available.
 *
 * This one uses the
 */
public class SessionFreeFormAuthenticator extends LoginAuthenticator {

    private static final Logger log = Logger.getLogger(SessionFreeFormAuthenticator.class);

    private final AuthenticationCache authenticationCache;

    public SessionFreeFormAuthenticator() {
        this(STATIC_AUTHENTICATION_CACHE);
    }

    public SessionFreeFormAuthenticator(AuthenticationCache authenticationCache) {
        this.authenticationCache = authenticationCache;
    }

    @Override
    public Authentication validateRequest(ServletRequest request, ServletResponse response, boolean mandatory) throws ServerAuthException {
        HttpServletResponse httpResponse = (HttpServletResponse)response;

        log.info("Ooh, Im in charge now!!");


        respondWithForbiddenMessage(httpResponse);


        return SEND_FAILURE;
    }

    private void respondWithForbiddenMessage(HttpServletResponse httpResponse) {
        httpResponse.setContentType("text/html");
        httpResponse.setStatus(403);
        PrintWriter writer = null;
        try {
            writer = httpResponse.getWriter();
            writer.println("<html><body><h1>(403 - Forbidden) BAD ROBOT - NO GO HERE!</h1></body></html>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(writer);
        }
    }

    @Override public String getAuthMethod() {
        return "FORM";
    }

    @Override
    public boolean secureResponse(ServletRequest request, ServletResponse response, boolean mandatory, Authentication.User validatedUser) throws ServerAuthException {
        return true;
    }


}