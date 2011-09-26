package ixcode.platform.http.representation;

import java.net.*;

public interface HypermediaType {
    Hyperlink getRelationHyperlink(String type);
}