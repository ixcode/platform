package ixcode.platform.build;

import ixcode.platform.io.RelativeFile;
import ixcode.platform.text.format.UriFormat;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.lang.System.getProperty;
import static org.apache.log4j.Logger.getLogger;

public class ResolveDependencies implements BuildTask {
    private static final Logger log = getLogger(ResolveDependencies.class);

    private final Module module;
    private final RelativeFile productionLibDir;

    private final List<DependencyRepo> searchRepositories;

    public ResolveDependencies(Module module, RelativeFile productionLibDir) {
        this.module = module;
        this.productionLibDir = productionLibDir;
        this.searchRepositories = buildRepoList(module);
    }

    @Override public void execute(BuildLog buildLog) {
        for (MavenArtifact d : module.productionDeps) {
            resolveDependency(d, buildLog);
        }
    }

    private void resolveDependency(MavenArtifact d, BuildLog buildLog) {
        boolean success = false;
        for (DependencyRepo repo : searchRepositories) {
            if (repo.resolveDependencyTo(d, productionLibDir)) {
                success = true;
                break;
            }
        }

        if (!success) {
            throw new RuntimeException(format("Could not resolve dependency [%s] in either local or remote repositories", d));
        }
    }


    private static List<DependencyRepo> buildRepoList(Module module) {
        File localMvnRepo = new File(format("%s/%s", getProperty("user.home"), ".m2/repository"));

        List<DependencyRepo> repoList = new ArrayList<DependencyRepo>(module.repositories);

        repoList.add(0, new DependencyRepo(new UriFormat().parseString("http://mvn.repo")));

        if (localMvnRepo.exists()) {
            repoList.add(0, new DependencyRepo(localMvnRepo.toURI()));
        }

        return repoList;
    }
}