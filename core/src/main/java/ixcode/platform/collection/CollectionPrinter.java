package ixcode.platform.collection;

import java.util.Collection;
import java.util.List;

public class CollectionPrinter<T> {

    private final FList<T> list;
    private final String separator;

    public static <T> String printCollection(Collection<T> collection) {
        return printCollection(collection, ", ");
    }

    public static <T> String printCollection(Collection<T> collection, String separator) {
        return new CollectionPrinter<T>(collection, separator).toString();
    }

    private CollectionPrinter(Collection<T> collection, String separator) {
        this.list = new FArrayList<T>(collection);
        this.separator = separator;
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder();
        list.apply(new Action<T>() {
            @Override public void to(T item, Collection<T> tail) {
                String value = (item == null) ? "<null>" : item.toString();
                sb.append(value)
                  .append((tail.size() == 0) ? "" : separator);
            }
        });
        return sb.toString();
    }
}