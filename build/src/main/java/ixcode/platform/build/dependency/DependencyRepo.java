package ixcode.platform.build.dependency;

import ixcode.platform.build.BuildLog;
import ixcode.platform.build.task.Copy;
import ixcode.platform.io.ExecuteSystemCommand;
import ixcode.platform.io.RelativeFile;
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

    public boolean resolveDependencyTo(MavenArtifact dependency, RelativeFile libDir, BuildLog buildLog) {
        libDir.mkdirs();

        if ("file".equals(location.getScheme())) {
            return resolveLocalDependency(dependency, libDir, buildLog);
        }

        return false;
    }

    public boolean exists() {
        if ("file".equals(location.getScheme())) {
            return new File(location).exists();
        }
        return false;
    }

    private boolean resolveLocalDependency(MavenArtifact dependency, RelativeFile libDir, BuildLog buildLog) {
        URI sourceFilePath = new UriFormat().parseString(format("%s%s", new UriFormat().format(location),
                                                                dependency.toMavenRepositoryPath()));

        File sourceFile = new File(sourceFilePath);
        if (!sourceFile.exists()) {
            log.debug(format("File [%s] doesn't exist in local repo", sourceFile.getAbsolutePath()));
            return false;
        }

        log.debug(format("Going to copy [%s] to [%s]", sourceFilePath, libDir.getAbsolutePath()));

        File destFile = new File(libDir.getAbsolutePath(), dependency.toJarFileName());

        new Copy(sourceFile, destFile).execute(buildLog);

        if (!destFile.exists()) {
            throw new RuntimeException(format("Failed to copy file [%s] to [%s]", sourceFile.getAbsolutePath(), destFile.getAbsolutePath()));
        }

        return true;
    }

    public void publishArtifact(MavenArtifact artifact, RelativeFile artifactFile, BuildLog buildLog) {
        if ("file".equals(location.getScheme())) {
            publishLocalArtifact(location, artifact, artifactFile, buildLog);
        }
    }

    private void publishLocalArtifact(URI location, MavenArtifact artifact, RelativeFile artifactFile, BuildLog buildLog) {
        URI destFilePath = new UriFormat().parseString(format("%s%s", new UriFormat().format(location),
                                                                artifact.toMavenRepositoryPath()));

        buildLog.println("Going to publish artifact to [%s]", destFilePath);

        new Copy(artifactFile.asFile(), new File(destFilePath)).execute(buildLog);
        
    }


}