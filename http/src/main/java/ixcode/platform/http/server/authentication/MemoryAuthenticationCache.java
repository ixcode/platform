package ixcode.platform.http.server.authentication;

public class MemoryAuthenticationCache implements AuthenticationCache {
    public static final AuthenticationCache STATIC_AUTHENTICATION_CACHE = new MemoryAuthenticationCache();
}