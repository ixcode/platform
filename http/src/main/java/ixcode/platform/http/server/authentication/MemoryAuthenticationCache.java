package ixcode.platform.http.server.authentication;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthenticationCache implements AuthenticationCache {

    Map<String, String> map = new HashMap<String, String>();

    @Override public String getSessionSecret(String sessionId) {
        return map.get(sessionId);
    }

    @Override public void setSessionSecret(String sessionId, String encryptedSecret) {
       map.put(sessionId, encryptedSecret);
    }
}