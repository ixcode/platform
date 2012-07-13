package ixcode.platform.http.server.authentication;

public interface AuthenticationCache {
    String getSessionSecret(String sessionId);

    void setSessionSecret(String sessionId, String encryptedSecret);
}