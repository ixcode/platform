package ixcode.platform.http.server;

import ixcode.platform.io.*;
import ixcode.platform.reflect.*;
import org.apache.log4j.*;
import org.eclipse.jetty.http.security.*;
import org.eclipse.jetty.security.*;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.*;
import org.eclipse.jetty.servlet.*;

import javax.servlet.*;
import java.io.*;

import static ixcode.platform.logging.ConsoleLog4jLogging.*;
import static java.lang.String.*;

public class HttpServer {
    private static final Logger LOG = Logger.getLogger(HttpServer.class);

    private Server server;
    private final String contextPath;
    private Servlet rootServlet;
    private String serverName;
    private int httpPort;
    private String webrootDir;

    public HttpServer(String serverName, int port, ixcode.platform.http.RequestDispatcher rootServlet) {
        this(serverName, "web", port, "/", rootServlet);
    }

    public HttpServer(String serverName, String webrootDir,
                      int httpPort,
                      String contextPath,
                      Servlet rootServlet) {


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
        new HttpServer(args[0], "web", 8080, "/", loadServletClass(args[1])).start();
    }

    private static Servlet loadServletClass(String servletClassName) {
        return new ObjectFactory<Servlet>().instantiate(servletClassName);
    }


    public void start() {
        try {
            LOG.info(format("Starting Http Server [%s] on port [%d]...", serverName, httpPort));
            server = new Server(httpPort);
            server.setHandler(handlers());
            server.start();
            new SystemProcess().writeProcessIdToFile(".webserver.pid");
            LOG.info(format("Http Server Started. Serving using the dispatcher [%s], static content is from [%s]", rootServlet.getClass().getName(), new File(webrootDir).getCanonicalPath()));
            server.join();
        } catch (Exception e) {
            throw new HttpServerStartupException(e);
        }
    }


    private HandlerList handlers() {
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler(), servletHandler()});
        return handlers;
    }

    private ResourceHandler resourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(webrootDir);
        return resourceHandler;
    }

    private ServletContextHandler servletHandler() {
        ServletContextHandler servletHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);

        servletHandler.setContextPath(contextPath);
        servletHandler.setResourceBase(webrootDir);
        ErrorHandler errorHandler = new ErrorHandler();
        errorHandler.setServer(server);
        servletHandler.setErrorHandler(errorHandler);
        servletHandler.addServlet(new ServletHolder(rootServlet), "/*");
        return servletHandler;
    }


    private static ConstraintMapping mapConstraintTo(Constraint constraint, String path) {
        ConstraintMapping cm = new ConstraintMapping();
        cm.setPathSpec(path);
        cm.setConstraint(constraint);
        return cm;
    }
}