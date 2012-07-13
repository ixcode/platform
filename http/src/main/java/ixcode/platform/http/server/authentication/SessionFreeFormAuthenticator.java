package ixcode.platform.http.server.authentication;

import ixcode.platform.cryptography.AesEncryption;
import ixcode.platform.cryptography.Encryption;
import org.apache.log4j.Logger;
import org.eclipse.jetty.security.ServerAuthException;
import org.eclipse.jetty.security.authentication.LoginAuthenticator;
import org.eclipse.jetty.server.Authentication;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.UUID;

import static ixcode.platform.io.IoStreamHandling.closeQuietly;
import static org.eclipse.jetty.server.Authentication.SEND_FAILURE;

/**
 * The normal FormAuthenticator requires the use of the HttpSession, which is
 * not always available.
 *
 * This one uses an AuthenticationCache abstraction so that you could
 * put whatever behind it - maybe mongo.
 */
public class SessionFreeFormAuthenticator extends LoginAuthenticator {

    private static final Logger log = Logger.getLogger(SessionFreeFormAuthenticator.class);

    private static final AuthenticationCache STATIC_AUTHENTICATION_CACHE = new MemoryAuthenticationCache();
    private static final Encryption STATIC_AES_ENCRYPTION = new AesEncryption();

    private final AuthenticationCache authenticationCache;
    private final Encryption encryption;
    private final String cookieKey;


    public SessionFreeFormAuthenticator() {
        this(STATIC_AUTHENTICATION_CACHE, STATIC_AES_ENCRYPTION);
    }

    public SessionFreeFormAuthenticator(AuthenticationCache authenticationCache,
                                        Encryption encryption) {
        this.authenticationCache = authenticationCache;
        this.encryption = encryption;
        this.cookieKey = "session";
    }

    @Override
    public Authentication validateRequest(ServletRequest request, ServletResponse response, boolean mandatory) throws ServerAuthException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        try {
            log.info("Platform secure form authentication in operation");

            String secretKey = getSecretKey(httpRequest);

            if (secretKey == null) {
                String cookieSecret = generateSecret(request);
                log.info("I'm going to send the secret: " + cookieSecret);
                String encryptedSecret = encryption.encrypt(cookieSecret);
                Cookie secretCookie = new Cookie(cookieKey, encryptedSecret);
                secretCookie.setMaxAge(60000);

                httpResponse.addCookie(secretCookie);
            }


            respondWithUnauthorizedMessage(httpResponse);


            return SEND_FAILURE;
        } catch (Exception e) {
            log.error("Exception trying to validate request", e);
            throw new RuntimeException(e);
        }
    }

    private String getSecretKey(HttpServletRequest httpRequest) {

        Cookie[] cookies = httpRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieKey.equals(cookie.getName())) {
                    String secretKey = encryption.decrypt(cookie.getValue());
                    log.info("Hey I found the secure cookie!: " + secretKey);
                    return secretKey;
                }
            }
        }

        return null;
    }

    private String generateSecret(ServletRequest request) {
        return UUID.randomUUID().toString() + ":" + request.getRemoteAddr();
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


}