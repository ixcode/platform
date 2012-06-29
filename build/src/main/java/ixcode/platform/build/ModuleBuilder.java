package ixcode.platform.build;

import ixcode.platform.build.dependency.DependencyRepo;
import ixcode.platform.build.logging.ConsoleLog;
import ixcode.platform.build.task.Clean;
import ixcode.platform.build.task.Compilation;
import ixcode.platform.build.task.Copy;
import ixcode.platform.build.task.Jar;
import ixcode.platform.build.task.Publish;
import ixcode.platform.build.task.ResolveDependencies;
import ixcode.platform.build.task.Tar;
import ixcode.platform.io.RelativeFile;
import ixcode.platform.text.format.UriFormat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static ixcode.platform.build.Module.loadModule;
import static ixcode.platform.io.RelativeFile.relativeFile;
import static java.lang.String.format;
import static java.lang.System.getProperty;
import static java.util.Arrays.asList;

public class ModuleBuilder {

    private final BuildLog buildLog;
    private final File moduleDir;
    private final RelativeFile sourceDir;

    private final RelativeFile libDir;
    private final RelativeFile productionLibDir;
    private final RelativeFile resourcesDir;
    private final RelativeFile scriptDir;
    private final RelativeFile webDir;

    private final RelativeFile targetDir;
    private final RelativeFile targetClassesDir;
    private final RelativeFile targetDistDir;
    private final RelativeFile targetJarfile;
    private final RelativeFile targetLibDir;
    private final RelativeFile targetWebDir;

    private final Module module;
    private final RelativeFile targetTarball;
    private DependencyRepo localMavenRepo;



    public static void main(String[] args) {
        new ModuleBuilder(new ConsoleLog(), new File(args[0]))
                .clean()
                .build();
    }

    public ModuleBuilder(BuildLog consoleLog, File moduleDir) {
        this.buildLog = consoleLog;
        this.moduleDir = moduleDir;
        this.module = loadModule(moduleDir, buildLog);

        sourceDir = locateSourceDir(moduleDir);
        resourcesDir = relativeFile(moduleDir, "src/main/resource");
        scriptDir = relativeFile(moduleDir, "src/main/script/bash");
        webDir = relativeFile(moduleDir, "src/main/web");

        libDir = relativeFile(moduleDir, "lib/production");
        productionLibDir = relativeFile(moduleDir, "lib/production");


        targetDir = relativeFile(moduleDir, "target");
        targetClassesDir = relativeFile(moduleDir, "target/work/classes");
        targetDistDir = relativeFile(moduleDir, "target/dist");
        targetWebDir = relativeFile(moduleDir, "target/dist/web");
        targetLibDir = relativeFile(moduleDir, "target/dist/lib");

        targetJarfile = relativeFile(moduleDir, "target/dist/" + module.artifact.toJarFileName());
        targetTarball = relativeFile(moduleDir, "target/" + module.name + ".tar.gz");

        localMavenRepo = localRepo();

        buildLog.printTitle("Builder (v.10) - building now!");
    }

    private static RelativeFile locateSourceDir(File moduleDir) {
        RelativeFile sourceDir = relativeFile(moduleDir, "src/main/java");

        if (!sourceDir.exists()) {
            sourceDir = relativeFile(moduleDir, "src");
        }

        if (!sourceDir.exists()) {
            throw new RuntimeException("Could not locate your source, where have you put it?!");
        }

        return sourceDir;
    }

    public ModuleBuilder build() {

        buildLog.println("Module Dir [%s]", moduleDir);

        //new ResolveDependencies(module, productionLibDir, defaultRepos()).execute(buildLog);

        new Compilation(sourceDir, productionLibDir, targetClassesDir).execute(buildLog);
        new Copy(resourcesDir, targetClassesDir).execute(buildLog);

        new Jar(targetJarfile, targetClassesDir, resourcesDir).execute(buildLog);

        if (localMavenRepo.exists()) {
            //new Publish(localMavenRepo, module.artifact, targetJarfile).execute(buildLog);
        }

        new Copy(scriptDir, targetDistDir).execute(buildLog);
        new Copy(webDir, targetWebDir).execute(buildLog);

        new Copy(productionLibDir, targetLibDir).execute(buildLog);

        new Tar(targetTarball, targetDistDir).execute(buildLog);

        return this;
    }

    private List<DependencyRepo> defaultRepos() {
        List<DependencyRepo> defaultRepos = new ArrayList<DependencyRepo>();

        defaultRepos.add(new DependencyRepo(new UriFormat().parseString("http://mvn.repo")));

        if (localMavenRepo.exists()) {
            defaultRepos.add(localMavenRepo);
        }

        return defaultRepos;
    }

    private DependencyRepo localRepo() {File localMvnRepoFile = new File(format("%s/%s", getProperty("user.home"), ".m2/repository"));
        return new DependencyRepo(localMvnRepoFile.toURI());
    }

    public ModuleBuilder clean() {
        new Clean(targetDir).execute(buildLog);

        return this;
    }

}