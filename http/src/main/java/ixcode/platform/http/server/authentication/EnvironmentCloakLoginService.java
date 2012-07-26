package ixcode.platform.http.server.authentication;

import org.eclipse.jetty.http.security.Password;
import org.eclipse.jetty.security.MappedLoginService;
import org.eclipse.jetty.server.UserIdentity;


import java.io.IOException;

import static java.lang.System.getenv;

public class EnvironmentCloakLoginService extends MappedLoginService {

    private final static String USERNAME_KEY = "IX_CLOAK_USER";
    private final static String PASSWORD_KEY = "IX_CLOAK_PASSWORD";

    private final String username;
    private final Password password;


    public EnvironmentCloakLoginService() {
        username = getenv(USERNAME_KEY);
        password = new Password(getenv(PASSWORD_KEY));
    }

    @Override protected UserIdentity loadUser(String username) {
        return super.putUser(username, password, new String[] {"user"});
    }

    @Override protected void loadUsers() throws IOException {
        super.putUser(username, password, new String[] {"user"});
    }
}