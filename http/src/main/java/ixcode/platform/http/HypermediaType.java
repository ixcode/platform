package ixcode.platform.http;

import java.net.*;

public interface HypermediaType {
    URI getLinkOfType(String type);
}