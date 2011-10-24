package ixcode.platform.http.representation;

import java.net.URI;

public interface LinkCollection {
    void addHyperlink(URI uri, String relation, String title);
}