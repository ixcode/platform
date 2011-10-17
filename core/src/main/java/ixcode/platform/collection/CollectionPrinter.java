package ixcode.platform.collection;

import java.util.Collection;
import java.util.List;

public class CollectionPrinter<T> {

    private final FList<T> list;

    public static <T> String printCollection(Collection<T> collection) {
        return new CollectionPrinter<T>(collection).toString();
    }

    public CollectionPrinter(Collection<T> collection) {
        this.list = new FArrayList<T>(collection);
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder();
        list.apply(new Action<T>() {
            @Override public void to(T item, Collection<T> tail) {
                String value = (item == null) ? "<null>" : item.toString();
                String separator = (tail.size() == 0) ? "" : ", ";

                sb.append(value).append(separator);
            }
        });
        return sb.toString();
    }
}