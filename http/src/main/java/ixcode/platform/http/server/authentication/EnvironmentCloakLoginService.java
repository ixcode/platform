package ixcode.platform.http.server.authentication;

import org.apache.log4j.Logger;
import org.eclipse.jetty.http.security.Password;
import org.eclipse.jetty.security.MappedLoginService;
import org.eclipse.jetty.server.UserIdentity;

import java.io.IOException;

import static java.lang.System.getenv;
import static org.apache.log4j.Logger.getLogger;

public class EnvironmentCloakLoginService extends MappedLoginService {

    private static final Logger log = getLogger(EnvironmentCloakLoginService.class);

    private final static String USERNAME_KEY = "IX_CLOAK_USER";
    private final static String PASSWORD_KEY = "IX_CLOAK_PASSWORD";

    private final String username;
    private final Password password;


    public EnvironmentCloakLoginService() {
        username = getenv(USERNAME_KEY);
        password = new Password(getenv(PASSWORD_KEY));
        log.info("Running with Username : " + username + " : " + password.toString());
    }

    @Override protected UserIdentity loadUser(String username) {
        return super.putUser(username, password, new String[] {"user"});
    }

    @Override protected void loadUsers() throws IOException {
        super.putUser(username, password, new String[] {"user"});
    }
}