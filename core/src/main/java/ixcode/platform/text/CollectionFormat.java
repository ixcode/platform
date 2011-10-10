package ixcode.platform.text;

import org.mockito.internal.matchers.*;

import java.util.*;

public class CollectionFormat extends AbstractFormat<Collection<?>> {

    public static String collectionToString(Collection<?> source) {
        return new CollectionFormat().format(source);
    }

    @Override
    public Collection parseString(String source) {
        throw new UnsupportedOperationException("Not yet implemented. Not sure if it CAN be implemented?");
    }

    @Override
    public String format(Collection<?> source) {
        StringBuilder sb = new StringBuilder();
        for (Iterator<?> itr = source.iterator(); itr.hasNext();) {
            sb.append(itr.next().toString());
            if (itr.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}