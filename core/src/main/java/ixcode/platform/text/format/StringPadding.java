package ixcode.platform.text.format;

public class StringPadding {

    public static String padRight(String input, int width) {
        StringBuilder sb = new StringBuilder();

        String prefix = (input.length() <= width) ? input : input.substring(0, (width));

        sb.append(prefix);

        for (int i=0; i<(width-input.length());++i) {
            sb.append(" ");
        }

        return sb.toString();
    }
}