package ixcode.platform.http;

import java.net.*;

public class CreatedResponse extends Response {
    private URI locationHeader;

    public URI getLocationHeader() {
        return locationHeader;
    }
}