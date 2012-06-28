package ixcode.platform.build.task;

import ixcode.platform.build.BuildLog;
import ixcode.platform.build.BuildTask;
import ixcode.platform.build.dependency.DependencyRepo;
import ixcode.platform.build.dependency.MavenArtifact;
import ixcode.platform.io.RelativeFile;

public class Publish implements BuildTask {

    private DependencyRepo repo;
    private final MavenArtifact artifact;
    private final RelativeFile artifactFile;

    public Publish(DependencyRepo repo,
                   MavenArtifact artifact,
                   RelativeFile artifactFile) {
        this.repo = repo;

        this.artifact = artifact;
        this.artifactFile = artifactFile;
    }


    @Override public void execute(BuildLog buildLog) {
        repo.publishArtifact(artifact, artifactFile);
    }
}