package ixcode.platform.build;

import ixcode.platform.text.format.UriFormat;
import org.apache.log4j.Logger;

import java.net.URI;

import static java.lang.String.format;
import static org.apache.log4j.Logger.getLogger;

public class DependencyRepo {
    private static final Logger log = getLogger(DependencyRepo.class);

    public final URI location;

    public DependencyRepo(URI location) {
        this.location = location;
    }

    public boolean resolveDependencyTo(Dependency dependency, RelativeFile libDir) {
        libDir.mkdirs();

        if ("file".equals(location.getScheme())) {
            return resolveLocalDependency(dependency, libDir);
        }

        return false;
    }

    private boolean resolveLocalDependency(Dependency dependency, RelativeFile libDir) {
        String sourceFilePath = format("%s%s", new UriFormat().format(location),
                                       dependency.toMavenRepositoryPath());

        log.debug(format("Going to copy [%s] to [%s]", sourceFilePath, libDir.getAbsolutePath()));


        return false;
    }
}