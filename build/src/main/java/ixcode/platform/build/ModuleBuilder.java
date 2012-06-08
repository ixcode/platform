package ixcode.platform.build;

import java.io.File;

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


    public static void main(String[] args) {
        new ModuleBuilder(new ConsoleLog(), new File(args[0]))
                //.clean()
                .build();
    }

    public ModuleBuilder(BuildLog consoleLog, File moduleDir) {
        this.buildLog = consoleLog;
        this.moduleDir = moduleDir;
        this.moduleName = calculateModuleName(moduleDir);

        sourceDir = relativeFile(moduleDir, "src/main/java");
        resourcesDir = relativeFile(moduleDir, "src/main/resource");
        scriptDir = relativeFile(moduleDir, "src/main/script/bash");
        productionLibDir = relativeFile(moduleDir, "lib/production");


        targetDir = relativeFile(moduleDir, "target");
        targetClassesDir = relativeFile(moduleDir, "target/work/classes");
        targetDistDir = relativeFile(moduleDir, "target/dist");
        targetLibDir = relativeFile(moduleDir, "target/dist/lib");

        targetJarfile = relativeFile(moduleDir, "target/dist/lib/" + moduleName + ".jar");

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
    private static String calculateModuleName(File moduleDir) {
        RelativeFile relativeFile = new RelativeFile(moduleDir, moduleDir);

        File[] files = relativeFile.listAllFilesMatching(".git");
        if (files.length == 0) {
            return format("%s-%s", moduleDir.getParentFile().getName(), moduleDir.getName());
        }

        return moduleDir.getName();
    }


    public ModuleBuilder build() {

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