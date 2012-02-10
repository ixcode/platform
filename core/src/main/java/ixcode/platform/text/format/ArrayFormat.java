package ixcode.platform.text.format;

import java.util.Collection;
import java.util.Iterator;

public class ArrayFormat extends AbstractFormat {

    public static String arrayToString(Object[] source) {
        return new ArrayFormat().format(source);
    }

    @Override
    public <T> T parseString(String source) {
        throw new UnsupportedOperationException("Not yet implemented. Not sure if it CAN be implemented?");
    }

    @Override
    public String format(Object source) {
        Object[] array = (Object[]) source;
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<array.length;++i) {
            sb.append(array[i].toString());
            if (i<array.length-1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }


    
}