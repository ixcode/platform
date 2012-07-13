package ixcode.platform.http.server.authentication;

import org.eclipse.jetty.server.UserIdentity;

public class Session {
    private final SessionValidity validity;
    private final String sessionId;

    public final String secret;
    public final UserIdentity user;

    public static Session invalid() {
        return new Session(SessionValidity.INVALID);
    }

    public static Session valid(String sessionId, String secret, UserIdentity user) {
        return new Session(SessionValidity.VALID, sessionId, secret, user);
    }

    public boolean isValidSecret(String encryptedSecret) {
        return secret.equals(encryptedSecret);
    }

    private enum SessionValidity {
        VALID, INVALID;
    }

    Session(SessionValidity validity) {
        this(validity, null, null, null);
    }

    Session(SessionValidity validity,
            String sessionId,
            String secret,
            UserIdentity user) {
        this.validity = validity;
        this.sessionId = sessionId;
        this.secret = secret;
        this.user = user;
    }

    public boolean isInvalid() {
        return SessionValidity.INVALID.equals(validity);
    }

}