package ixcode.platform.http.server.authentication;

import ixcode.platform.cryptography.AesEncryption;
import ixcode.platform.cryptography.Encryption;
import ixcode.platform.http.server.CookieJar;
import org.apache.log4j.Logger;
import org.eclipse.jetty.security.ServerAuthException;
import org.eclipse.jetty.security.UserAuthentication;
import org.eclipse.jetty.security.authentication.LoginAuthenticator;
import org.eclipse.jetty.security.authentication.SessionAuthentication;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.server.UserIdentity;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import static ixcode.platform.http.server.CookieJar.cookieJarFrom;
import static ixcode.platform.io.IoStreamHandling.closeQuietly;
import static org.eclipse.jetty.server.Authentication.NOT_CHECKED;
import static org.eclipse.jetty.server.Authentication.SEND_CONTINUE;

/**
 * The normal FormAuthenticator requires the use of the HttpSession, which is not always available.
 * <p/>
 * This one uses an AuthenticationCache abstraction so that you could put whatever behind it - maybe mongo.
 */
public class SessionFreeFormAuthenticator extends LoginAuthenticator {

    private static final Logger log = Logger.getLogger(SessionFreeFormAuthenticator.class);

    private static final AuthenticationCache STATIC_AUTHENTICATION_CACHE = new MemoryAuthenticationCache();
    private static final Encryption STATIC_AES_ENCRYPTION = new AesEncryption();

    private static final String SESSION_KEY = "_token";
    private static final String SESSION_ID = "_id";

    private final AuthenticationCache authenticationCache;
    private final Encryption encryption;

    private String loginRedirectPath = "/auth/login";


    public SessionFreeFormAuthenticator() {
        this(STATIC_AUTHENTICATION_CACHE, STATIC_AES_ENCRYPTION);
    }

    public SessionFreeFormAuthenticator(AuthenticationCache authenticationCache,
                                        Encryption encryption) {
        this.authenticationCache = authenticationCache;
        this.encryption = encryption;

    }

    @Override
    public Authentication validateRequest(ServletRequest request, ServletResponse response, boolean mandatory) throws ServerAuthException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        CookieJar cookieJar = cookieJarFrom(httpRequest);

        try {
            log.info("SECURE-CHECK: " + httpRequest.getRequestURI());


            if (isLoginHandler(httpRequest)) {
                if ("POST".equals(httpRequest.getMethod())) {
                    UserIdentity user = handleLoginSubmission(httpRequest, httpResponse);
                    if (user != null) {
                        startSession(httpRequest, httpResponse, user);
                        redirectToRequestedPage(httpRequest, cookieJar, httpResponse);
                        return new FormAuthentication("FORM", user);
                    }
                    redirectToLoginPage(httpRequest, cookieJar, httpResponse);
                    return SEND_CONTINUE;
                }
                return NOT_CHECKED;
            }

            Session session = getSession(cookieJar);

            if (session.isInvalid()) {
                redirectToLoginPage(httpRequest, cookieJar, httpResponse);
                return SEND_CONTINUE;
            }


            return new SessionAuthentication("FORM", session.user, session.secret);

        } catch (Exception e) {
            log.error("Exception trying to validate request", e);
            throw new RuntimeException(e);
        }
    }

    private void redirectToRequestedPage(HttpServletRequest httpRequest, CookieJar cookieJar, HttpServletResponse httpResponse) {
        log.info("Redirecting back to requested page...");
        String redirectPath = "/";
        if (cookieJar.contains("_loginSource")) {
            redirectPath = cookieJar.get("_loginSource");
            Cookie redirectBackToCookie = new Cookie("_loginSource", redirectPath);
            redirectBackToCookie.setPath("/");
            redirectBackToCookie.setMaxAge(0);
            httpResponse.addCookie(redirectBackToCookie);
        }

        try {
            httpResponse.sendRedirect(redirectPath);
        } catch (IOException e) {
            log.error("Failed to redirect", e);
            throw new RuntimeException(e);
        }

    }

    private void redirectToLoginPage(HttpServletRequest httpRequest,
                                     CookieJar cookieJar, HttpServletResponse httpResponse) {

        log.info("Redirecting to the the login handler:");
        try {
            if (!cookieJar.contains("_loginSource")) {
                Cookie redirectBackToCookie = new Cookie("_loginSource", httpRequest.getRequestURI());
                redirectBackToCookie.setPath("/");
                httpResponse.addCookie(redirectBackToCookie);
            }
            httpResponse.sendRedirect(this.loginRedirectPath);
        } catch (IOException e) {
            log.error("Failed to redirect", e);
            throw new RuntimeException(e);
        }

    }

    private UserIdentity handleLoginSubmission(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        String username = httpRequest.getParameter("x_username");
        String password = httpRequest.getParameter("x_password");

        if (username == null || password == null) {
            throw new RuntimeException("Could not process login request, you need to specify the fields x_username and x_password");
        }

        return  _loginService.login(username, password);
    }

    private boolean isLoginHandler(HttpServletRequest httpRequest) {
        return loginRedirectPath.equals(httpRequest.getRequestURI());
    }

    private Session getSession(CookieJar cookieJar) {

        String sessionId = cookieJar.get(SESSION_ID);
        String encryptedSecret = cookieJar.get(SESSION_KEY);
        String secretKey = (encryptedSecret == null) ? null : tryToDecryptSessionKey(encryptedSecret);

        if (secretKey == null || sessionId == null) {
            return Session.invalid();
        }

        Session session = authenticationCache.getSession(sessionId);
        if (session.isValidSecret(encryptedSecret)) {
            return session;
        }

        return Session.invalid();

    }

    private String tryToDecryptSessionKey(String encryptedSecret) {
        try {
            return encryption.decrypt(encryptedSecret);
        } catch (Exception e) {
            return null; // If we get an exception probably out of date encryption, treat as invalid
        }
    }

    private void startSession(HttpServletRequest httpRequest,
                              HttpServletResponse httpResponse,
                              UserIdentity user) {

        String sessionId = generateSessionId();
        String cookieSecret = generateSecret(sessionId, httpRequest);
        String encryptedSecret = encryption.encrypt(cookieSecret);

        log.info("I'm going to send the secret: " + cookieSecret);

        Cookie sessionIdCookie = new Cookie("_id", sessionId);
        sessionIdCookie.setPath("/");
        sessionIdCookie.setMaxAge(60000);

        Cookie secretCookie = new Cookie("_token", encryptedSecret);
        secretCookie.setPath("/");
        secretCookie.setMaxAge(60000);

        httpResponse.addCookie(sessionIdCookie);
        httpResponse.addCookie(secretCookie);

        authenticationCache.createSession(sessionId, encryptedSecret, user);

    }

    private String generateSessionId() {
        return UUID.randomUUID().toString();
    }


    private static String generateSecret(String sessionId, HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String accept = request.getHeader("Accept");
        return sessionId + ":" + request.getRemoteAddr() + ":" + request.getRemoteHost() + ":" + userAgent + ":" + accept;
    }

    private void respondWithUnauthorizedMessage(HttpServletResponse httpResponse) {
        httpResponse.setContentType("text/html");
        httpResponse.setStatus(401);
        PrintWriter writer = null;
        try {
            writer = httpResponse.getWriter();
            writer.println("<html><body><h1>(401 - Unauthorized) BAD ROBOT - NO GO HERE!</h1></body></html>");
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

    public static class FormAuthentication extends UserAuthentication implements Authentication.ResponseSent
    {
        public FormAuthentication(String method, UserIdentity userIdentity)
        {
            super(method,userIdentity);
        }

        @Override
        public String toString()
        {
            return "Form"+super.toString();
        }
    }

}