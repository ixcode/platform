package ixcode.platform.collection;

import java.util.*;

import static java.util.Arrays.asList;

public class SetManipulation {

    public static <T> HashSet<T> hashSetOf(T... items) {
        return new HashSet<T>(asList(items));
    }

    public static <T> Set<T> intersectionOf(Set<T> source, Set<T> target) {
        HashSet<T> result = new HashSet<T>();

        for (T item : target) {
            if (source.contains(item)) {
                result.add(item);
            }
        }

        return result;
    }

    public static <S, T> Set<T> intersectionOf(Set<S> source, Set<T> target, ItemMatcher<S, T> itemMatcher) {
        HashSet<T> result = new HashSet<T>();
        for (T targetItem : target) {
            for (S sourceItem : source) {
                if (itemMatcher.matches(sourceItem, targetItem)) {
                    result.add(targetItem);
                    break;
                }
            }
        }
        return result;
    }
}