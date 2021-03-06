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
        List<T> items = new ArrayList<T>(collection);

        while(items.size() > 0) {
            List<T> remaining = items.size() == 1 ? new ArrayList<T>() : items.subList(1, items.size());
            action.to(items.get(0), remaining);
            items = remaining;
        }
    }
}