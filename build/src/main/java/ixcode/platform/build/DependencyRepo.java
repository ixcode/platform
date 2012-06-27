package ixcode.platform.build;

import java.net.URI;
import java.net.URL;

public class DependencyRepo {
    public final URI location;

    public DependencyRepo(URI location) {
        this.location = location;
    }
}