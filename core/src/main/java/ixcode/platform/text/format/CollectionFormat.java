package ixcode.platform.text.format;

import java.util.*;

public class CollectionFormat extends AbstractFormat {

    public static String collectionToString(Collection<?> source) {
        return new CollectionFormat().format(source);
    }

    @Override
    public <T> T parseString(String source) {
        throw new UnsupportedOperationException("Not yet implemented. Not sure if it CAN be implemented?");
    }

    @Override
    public String format(Object source) {
        Collection<?> collection = (Collection<?>)source;
        StringBuilder sb = new StringBuilder();
        for (Iterator<?> itr = collection.iterator(); itr.hasNext();) {
            sb.append(itr.next().toString());
            if (itr.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }


}