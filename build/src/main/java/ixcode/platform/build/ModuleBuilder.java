package ixcode.platform.build;

import ixcode.platform.io.RelativeFile;

import java.io.File;

import static ixcode.platform.build.Module.loadModule;
import static ixcode.platform.io.RelativeFile.relativeFile;

public class ModuleBuilder {

    private final BuildLog buildLog;
    private final File moduleDir;
    private final RelativeFile sourceDir;

    private final RelativeFile libDir;
    private final RelativeFile productionLibDir;
    private final RelativeFile resourcesDir;
    private final RelativeFile scriptDir;
    private final RelativeFile targetDir;
    private final RelativeFile targetClassesDir;
    private final RelativeFile targetDistDir;
    private final RelativeFile targetJarfile;
    private final RelativeFile targetLibDir;

    private final Module module;
    private final RelativeFile targetTarball;


    public static void main(String[] args) {
        new ModuleBuilder(new ConsoleLog(), new File(args[0]))
                //.clean()
                .build();
    }

    public ModuleBuilder(BuildLog consoleLog, File moduleDir) {
        this.buildLog = consoleLog;
        this.moduleDir = moduleDir;
        this.module = loadModule(moduleDir, buildLog);

        sourceDir = relativeFile(moduleDir, "src/main/java");
        resourcesDir = relativeFile(moduleDir, "src/main/resource");
        scriptDir = relativeFile(moduleDir, "src/main/script/bash");

        libDir = relativeFile(moduleDir, "lib-ibx/production");
        productionLibDir = relativeFile(moduleDir, "lib-ibx/production");


        targetDir = relativeFile(moduleDir, "target");
        targetClassesDir = relativeFile(moduleDir, "target/work/classes");
        targetDistDir = relativeFile(moduleDir, "target/dist");
        targetLibDir = relativeFile(moduleDir, "target/dist/lib");

        targetJarfile = relativeFile(moduleDir, "target/dist/" + module.name + ".jar");
        targetTarball = relativeFile(moduleDir, "target/" + module.name + ".tar.gz");

        buildLog.printTitle("Builder (v.10) - building now!");
    }

    public ModuleBuilder build() {

        buildLog.println("Module Dir [%s]", moduleDir);

        new ResolveDependencies(module, productionLibDir).execute(buildLog);

        new Compilation(sourceDir, productionLibDir, targetClassesDir).execute(buildLog);
        new Copy(resourcesDir, targetClassesDir).execute(buildLog);

        new Jar(targetJarfile, targetClassesDir, resourcesDir).execute(buildLog);
        new Copy(scriptDir, targetDistDir).execute(buildLog);
        new Copy(productionLibDir, targetLibDir).execute(buildLog);

        new Tar(targetTarball, targetDistDir).execute(buildLog);

        return this;
    }

    public ModuleBuilder clean() {
        new Clean(targetDir).execute(buildLog);

        return this;
    }

}