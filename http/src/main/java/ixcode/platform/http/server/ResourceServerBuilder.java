package ixcode.platform.http.server;

import ixcode.platform.http.protocol.ContentType;

import static ixcode.platform.http.protocol.IanaContentType.json;

public class ResourceServerBuilder {

    private String serverName = ResourceServer.class.getSimpleName();
    private String hostname = "localhost";
    private int port = 8080;
    private String rootResourcePackageName;
    private ContentType defaultContentType = json;

    public static ResourceServerBuilder resourceServer() {
        return new ResourceServerBuilder();        
    }
    
    public ResourceServer build() {
        return new ResourceServer(serverName, hostname, port, rootResourcePackageName, defaultContentType);
    }

    public ResourceServerBuilder onPort(int port) {
        this.port = port;
        return this;
    }

    public ResourceServerBuilder withResourcesInPackage(String rootResourcePackageName) {
        this.rootResourcePackageName = rootResourcePackageName;
        return this;
    }

    public ResourceServerBuilder jsonByDefault() {
        this.defaultContentType = json;
        return this;
    }

    public ResourceServerBuilder at(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public ResourceServerBuilder called(String serverName) {
        this.serverName = serverName;
        return this;
    }
}