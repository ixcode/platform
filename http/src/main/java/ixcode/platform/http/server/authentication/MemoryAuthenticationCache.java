package ixcode.platform.http.server.authentication;

import org.eclipse.jetty.server.UserIdentity;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthenticationCache implements AuthenticationCache {

    Map<String, Session> map = new HashMap<String, Session>();

    @Override public Session getSession(String sessionId) {
        return map.get(sessionId);
    }

    @Override public Session createSession(String sessionId, String encryptedSecret, UserIdentity user) {
        Session session = Session.valid(sessionId, encryptedSecret, user);
        map.put(sessionId, session);
        return session;
    }
}