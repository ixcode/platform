package ixcode.platform.build;

import java.io.File;
import java.util.Map;

import static ixcode.platform.build.Module.loadModule;
import static ixcode.platform.build.RelativeFile.relativeFile;
import static java.lang.String.format;

public class ModuleBuilder {

    private final BuildLog buildLog;
    private final File moduleDir;
    private final RelativeFile sourceDir;
    private final RelativeFile productionLibDir;
    private final RelativeFile resourcesDir;
    private final RelativeFile scriptDir;
    private final RelativeFile targetDir;
    private final RelativeFile targetClassesDir;
    private final RelativeFile targetDistDir;
    private final RelativeFile targetJarfile;
    private final RelativeFile targetLibDir;
    private final String moduleName;
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
        this.moduleName = module.name;

        sourceDir = relativeFile(moduleDir, "src/main/java");
        resourcesDir = relativeFile(moduleDir, "src/main/resource");
        scriptDir = relativeFile(moduleDir, "src/main/script/bash");
        productionLibDir = relativeFile(moduleDir, "lib/production");


        targetDir = relativeFile(moduleDir, "target");
        targetClassesDir = relativeFile(moduleDir, "target/work/classes");
        targetDistDir = relativeFile(moduleDir, "target/dist");
        targetLibDir = relativeFile(moduleDir, "target/dist/lib");

        targetJarfile = relativeFile(moduleDir, "target/dist/" + moduleName + ".jar");
        targetTarball = relativeFile(moduleDir, "target/" + moduleName + ".tar.gz");

        buildLog.printTitle("Builder (v.10) - building now!");
    }


    /**
     * <p/>
     * If we are in a root git module then we will just use our dir name. If we are in a sub dir of a git module then we
     * will add the parent dir..
     * <p/>
     * e.g.
     * <p/>
     * <pre>
     * /platform
     *   .git
     *   /build --> platform-build.jar
     *   /core  --> platform-core.jar
     *
     * /someproject --> someproject.jar
     *   .git
     * </pre>
     */
    private String getModuleName(File moduleDir,
                                        Map<String, String> config) {

        if (config.containsKey("module.name")) {
            return config.get("module.name");
        }

        return moduleDir.getName();
    }


    public ModuleBuilder build() {

        buildLog.println("Module Dir [%s]", moduleDir);

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