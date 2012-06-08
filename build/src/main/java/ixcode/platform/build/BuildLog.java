package ixcode.platform.build;

public interface BuildLog {

    public void printTitle(String format, Object... parameters);

    public void println(String format, Object... parameters);
}