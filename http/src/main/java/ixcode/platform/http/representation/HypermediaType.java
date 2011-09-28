package ixcode.platform.http.representation;

import java.net.*;
import java.util.*;

public interface HypermediaType {
    List<Hyperlink> getRelationHyperlinks(String type);
}