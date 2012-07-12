package ixcode.platform.http.server;

import ixcode.platform.http.server.redirection.Redirection;
import ixcode.platform.io.SystemProcess;
import ixcode.platform.reflect.ObjectFactory;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.security.Constraint;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
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
    private ConstraintSecurityHandler securityHandler;


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
        HandlerList handlers = new HandlerList();

        handlers.setHandlers(new Handler[]{
                redirectionHandler(),
                resourceHandler(),
                servletHandler()});

        if (securityHandler != null) {
            securityHandler.setHandler(handlers);
            return securityHandler;
        }

        return handlers;
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
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);

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


    public HttpServer servingSassFrom(String sassRoom) {

        return this;
    }

    public HttpServer withRedirection(Redirection redirection) {
        this.redirections.add(redirection);
        return this;
    }


    public HttpServer basicAuthenticationFrom(String fileName) {

        File f = getCannonicalFileFor(fileName);

        if (!f.exists()) {
            return this;
        }


        HashLoginService loginService = new HashLoginService(this.serverName, f.getAbsolutePath());
        try {
            loginService.loadUsers();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Constraint constraint = new Constraint();
        constraint.setName(Constraint.__BASIC_AUTH);
        constraint.setAuthenticate(true);
        constraint.setRoles(new String[]{"user"});


        ConstraintMapping root = mapConstraintTo(constraint, "/*");


        BasicAuthenticator authenticator = new BasicAuthenticator();

        securityHandler = new ConstraintSecurityHandler();
        securityHandler.setRealmName("someRealm");
        securityHandler.setAuthenticator(authenticator);
        securityHandler.setConstraintMappings(asList(root));
        securityHandler.setLoginService(loginService);

        return this;
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