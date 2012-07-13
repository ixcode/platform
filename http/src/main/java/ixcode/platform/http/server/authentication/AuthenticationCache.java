package ixcode.platform.http.server.authentication;

import org.eclipse.jetty.server.UserIdentity;

public interface AuthenticationCache {
    Session getSession(String sessionId);

    Session createSession(String sessionId, String encryptedSecret, UserIdentity user);
}