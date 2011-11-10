package ixcode.platform.text;

import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;

public class CamelCaseTransform {

    public static String splitCamelCase(String input, String separator) {
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (isUpperCase(c)) {
               sb.append(separator).append(toLowerCase(c));
            } else {
               sb.append(c);
            }
        }
       return sb.toString();
    }

}