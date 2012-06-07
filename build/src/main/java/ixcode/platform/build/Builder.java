package ixcode.platform.build;

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

        RelativeFile sourceDir = new RelativeFile(moduleDir, "src/main/java");

        RelativeFile targetDir = new RelativeFile(moduleDir, "target");

        RelativeFile targetClassesDir = new RelativeFile(moduleDir, "target/classes");

        new Clean(targetDir).execute();
        new Compilation(buildLog, sourceDir, targetClassesDir).execute();
    }

}