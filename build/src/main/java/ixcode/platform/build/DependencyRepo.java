package ixcode.platform.build;

import ixcode.platform.io.ExecuteSystemCommand;
import ixcode.platform.text.format.UriFormat;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.URI;

import static ixcode.platform.io.ExecuteSystemCommand.executeSystemCommand;
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
        URI sourceFilePath = new UriFormat().parseString(format("%s%s", new UriFormat().format(location),
                                       dependency.toMavenRepositoryPath()));

        File sourceFile = new File(sourceFilePath);
        if (!sourceFile.exists()) {
            log.debug(format("File [%s] doesn't exist in local repo", sourceFile.getAbsolutePath()));
            return false;
        }

        log.debug(format("Going to copy [%s] to [%s]", sourceFilePath, libDir.getAbsolutePath()));

        File destFile = new File(libDir.getAbsolutePath(), dependency.toJarFileName());

        executeSystemCommand(format("cp %s %s", sourceFile.getAbsolutePath(), libDir.getAbsolutePath()),
                             libDir.getParentFile(),
                             new ExecuteSystemCommand.OutputHandler() {
                                 @Override public void handleLine(String line) {
                                     log.debug(line);
                                 }
                             });

        if (!destFile.exists()) {
            throw new RuntimeException(format("Failed to copy file [%s] to [%s]", sourceFile.getAbsolutePath(), destFile.getAbsolutePath()));
        }

        return true;
    }
}