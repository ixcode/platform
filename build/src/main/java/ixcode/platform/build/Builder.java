package ixcode.platform.build;

import static ixcode.platform.build.RelativeFile.relativeFile;

public class Builder {

    private BuildLog buildLog;

    public static void main(String[] args) {
        new Builder(new ConsoleLog()).buildModule(args[0]);
    }

    public Builder(BuildLog consoleLog) {
        this.buildLog = consoleLog;
    }


    public void buildModule(String moduleDir) {
        buildLog.println("Builder (v.10) - building now!");
        buildLog.println("Module Dir [%s]", moduleDir);

        RelativeFile sourceDir = relativeFile(moduleDir, "src/main/java");
        RelativeFile targetDir = relativeFile(moduleDir, "target");
        RelativeFile targetClassesDir = relativeFile(moduleDir, "target/classes");

        new Clean(buildLog, targetDir).execute();
        new Compilation(buildLog, sourceDir, targetClassesDir).execute();
    }

}