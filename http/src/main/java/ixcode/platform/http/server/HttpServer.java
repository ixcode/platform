package ixcode.platform.http.server;

import ixcode.platform.http.server.authentication.EnvironmentCloakLoginService;
import ixcode.platform.http.server.authentication.SessionFreeFormAuthenticator;
import ixcode.platform.http.server.redirection.Redirection;
import ixcode.platform.io.SystemProcess;
import ixcode.platform.reflect.ObjectFactory;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.security.Constraint;
import org.eclipse.jetty.security.Authenticator;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.authentication.DigestAuthenticator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ixcode.platform.logging.ConsoleLog4jLogging.initialiseLog4j;
import static java.lang.String.format;
import static java.lang.System.getProperty;
import static java.net.InetAddress.getLocalHost;
import static java.util.Arrays.asList;
import static org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS;

public class HttpServer {
    private static final Logger log = Logger.getLogger(HttpServer.class);

    private Server server;
    private final String contextPath;
    private Servlet rootServlet;
    private String hostname;
    private String serverName;
    private int httpPort;
    private String webrootDir;
    private List<Redirection> redirections = new ArrayList<Redirection>();
    private ConstraintSecurityHandler securityCloak;
    private String sassRoot;


    public HttpServer(Class serverClass, int port, Servlet rootServlet) {
        this(serverClass.getSimpleName(), "localhost", "web", port, "/", rootServlet);
    }

    public HttpServer(String serverName, String hostname, int port, Servlet rootServlet) {
        this(serverName, hostname, "web", port, "/", rootServlet);
    }

    public HttpServer(String serverName,
                      String hostname,
                      String webrootDir,
                      int httpPort,
                      String contextPath,
                      Servlet rootServlet) {

        this.hostname = hostname;
        this.serverName = serverName;
        this.httpPort = httpPort;
        this.webrootDir = webrootDir;
        this.contextPath = contextPath;
        this.rootServlet = rootServlet;
    }

    public HttpServer servingStaticContentFrom(String webrootDir) {
        this.webrootDir = webrootDir;
        return this;
    }

    public HttpServer servingSassFrom(String sassRoot) {
        this.sassRoot = sassRoot;
        return this;
    }

    public static void main(String args[]) {
        initialiseLog4j();
        new HttpServer(args[0], "localhost", "web", 8080, "/", loadServletClass(args[1])).start();
    }

    private static Servlet loadServletClass(String servletClassName) {
        return new ObjectFactory<Servlet>().instantiate(servletClassName);
    }


    public void start() {
        try {
            log.info(format("Starting Http Server [%s] on port [%d]...", serverName, httpPort));
            log.info(format("Serving from http://%s:%d/", hostname, httpPort));
            log.info(format("Running on host [%s]", getLocalHost().getHostName()));

            server = new Server(httpPort);

            server.setHandler(handler());


            server.start();
            new SystemProcess().writeProcessIdToFile(format(".webserver.%s.pid", serverName));

            log.info(format("Http Server Started. Serving using the dispatcher [%s] ", rootServlet.getClass().getName()));
            log.info((format("Static content is from [%s]", new File(webrootDir).getCanonicalPath())));

            server.join();

        } catch (Exception e) {
            throw new HttpServerStartupException(e);
        }
    }

    private Handler handler() {
        HandlerList handlerList = new HandlerList();

        Handler[] handlers = (sassRoot != null)
                ? new Handler[]{redirectionHandler(), resourceHandler(), sassHandler(), servletHandler()}
                : new Handler[]{redirectionHandler(), resourceHandler(), servletHandler()};

        handlerList.setHandlers(handlers);

        if (securityCloak != null) {
            securityCloak.setHandler(handlerList);
            return securityCloak;
        }

        return handlerList;
    }

    private Handler sassHandler() {
        return new SassHandler(new File(sassRoot));
    }

    private Handler redirectionHandler() {
        return new RedirectionHandler(redirections);
    }

    private ResourceHandler resourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(webrootDir);

        return resourceHandler;
    }

    private ServletContextHandler servletHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(NO_SESSIONS);


        servletContextHandler.setContextPath(contextPath);
        servletContextHandler.setResourceBase(webrootDir);

        ErrorHandler errorHandler = new ErrorHandler();
        errorHandler.setServer(server);
        servletContextHandler.setErrorHandler(errorHandler);

        servletContextHandler.addServlet(new ServletHolder(rootServlet), "/*");

        return servletContextHandler;
    }

    private static ConstraintMapping mapConstraintTo(Constraint constraint, String path) {
        ConstraintMapping cm = new ConstraintMapping();
        cm.setPathSpec(path);
        cm.setConstraint(constraint);
        return cm;
    }


    public HttpServer withRedirection(Redirection redirection) {
        this.redirections.add(redirection);
        return this;
    }


    /**
     * We are not even going to think about basic auth, although you could see how to do it.
     * <p/>
     * At least digest encrypts the password
     *
     * @See http://tools.ietf.org/html/rfc2617
     */
    public HttpServer cloakWithDigestAuth(String filename) {
        securityCloak = createFileBasedSecurityCloakHandler(filename, new DigestAuthenticator());

        return this;
    }

    public HttpServer cloakWithFormAuth(String filename) {
        SessionFreeFormAuthenticator formAuthenticator = new SessionFreeFormAuthenticator();

        securityCloak = createFileBasedSecurityCloakHandler(filename, formAuthenticator);

        return this;
    }

    public HttpServer cloakWithFormAuthFromEnv() {
        SessionFreeFormAuthenticator formAuthenticator = new SessionFreeFormAuthenticator();

        EnvironmentCloakLoginService loginService = new EnvironmentCloakLoginService();

        securityCloak = createSecurityHandler(formAuthenticator, loginService);

        return this;
    }

    private ConstraintSecurityHandler createFileBasedSecurityCloakHandler(String filename, Authenticator authenticator) {

        File f = getCannonicalFileFor(filename);
        if (!f.exists()) {
            return null;
        }
        HashLoginService loginService = new HashLoginService(this.serverName, f.getAbsolutePath());
        try {
            loginService.loadUsers();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return createSecurityHandler(authenticator, loginService);
    }

    private ConstraintSecurityHandler createSecurityHandler(Authenticator authenticator,
                                                            LoginService loginService) {
        Constraint constraint = new Constraint();
        constraint.setName(Constraint.__BASIC_AUTH);
        constraint.setAuthenticate(true);
        constraint.setRoles(new String[]{"user"});


        ConstraintMapping root = mapConstraintTo(constraint, "/*");

        ConstraintSecurityHandler handler = new ConstraintSecurityHandler();
        handler.setRealmName(this.serverName);
        handler.setAuthenticator(authenticator);
        handler.setConstraintMappings(asList(root));
        handler.setLoginService(loginService); return handler;
    }

    private File getCannonicalFileFor(String fileName) {
        try {
            return new File(fileName.replace("~", getProperty("user.home")))
                    .getCanonicalFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}