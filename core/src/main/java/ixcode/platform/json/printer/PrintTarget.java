package ixcode.platform.json.printer;

public interface PrintTarget {
    PrintTarget print(String text);

    PrintTarget print(String formatString, String... parameters);

    PrintTarget println(String s);

    PrintTarget indent();

    PrintTarget outdent();
}