package ixcode.platform.collection;

import java.util.*;

class Apply<T> {
    private Action<T> action;

    public static <T> Apply<T> the(Action<T> action) {
        return new Apply<T>(action);
    }

    private Apply(Action<T> action) {
        this.action = action;
    }

    public void to(Collection<T> collection) {
        for (T item : collection) {
            action.to(item);
        }
    }
}