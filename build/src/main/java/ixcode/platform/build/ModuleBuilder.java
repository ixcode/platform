package ixcode.platform.build;

import java.io.File;

import static ixcode.platform.build.RelativeFile.relativeFile;

public class ModuleBuilder {

    private final BuildLog buildLog;
    private File moduleDir;
    private RelativeFile sourceDir;
    private RelativeFile productionLibDir;
    private RelativeFile resourcesDir;
    private RelativeFile scriptDir;
    private RelativeFile targetDir;
    private RelativeFile targetClassesDir;
    private RelativeFile targetDistDir;
    private RelativeFile targetJarfile;
    private RelativeFile targetLibDir;



    public static void main(String[] args) {
        new ModuleBuilder(new ConsoleLog(), new File(args[0]))
                .clean()
                .build();
    }

    public ModuleBuilder(BuildLog consoleLog, File moduleDir) {
        this.buildLog = consoleLog;
        this.moduleDir = moduleDir;

        sourceDir = relativeFile(moduleDir, "src/main/java");
        resourcesDir = relativeFile(moduleDir, "src/main/resource");
        scriptDir = relativeFile(moduleDir, "src/main/script/bash");
        productionLibDir = relativeFile(moduleDir, "lib/production");


        targetDir = relativeFile(moduleDir, "target");
        targetClassesDir = relativeFile(moduleDir, "target/work/classes");
        targetDistDir = relativeFile(moduleDir, "target/dist");
        targetLibDir = relativeFile(moduleDir, "target/dist/lib");
        targetJarfile = relativeFile(moduleDir, "target/dist/lib/" + moduleDir.getName() + ".jar");

    }


    public ModuleBuilder build() {
        buildLog.println("Builder (v.10) - building now!");
        buildLog.println("Module Dir [%s]", moduleDir);

        new Compilation(sourceDir, productionLibDir, targetClassesDir).execute(buildLog);
        new CopyResources(resourcesDir, targetClassesDir).execute(buildLog);

        new Jar(targetJarfile, targetClassesDir, resourcesDir).execute(buildLog);
        new Copy(scriptDir, targetDistDir).execute(buildLog);
        new Copy(productionLibDir, targetLibDir).execute(buildLog);

        return this;
    }

    public ModuleBuilder clean() {
        new Clean(targetDir).execute(buildLog);

        return this;
    }

}