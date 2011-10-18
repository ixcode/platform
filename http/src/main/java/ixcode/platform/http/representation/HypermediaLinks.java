package ixcode.platform.http.representation;

import java.net.*;
import java.util.*;

public interface HypermediaLinks {
    List<Hyperlink> getHyperlinksMatching(String type);
}