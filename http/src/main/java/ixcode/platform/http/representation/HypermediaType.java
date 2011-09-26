package ixcode.platform.http.representation;

import java.net.*;

public interface HypermediaType {
    URI getRelationHyperlink(String type);
}