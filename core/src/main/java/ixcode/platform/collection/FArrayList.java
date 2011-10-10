package ixcode.platform.collection;

import java.util.*;

public class FArrayList<T> extends ArrayList<T> implements FList<T> {

    public FArrayList() {
    }

    public FArrayList(Collection<T> items) {
        super(items);
    }

    @Override
    public void apply(Action<T> action) {
        Apply.the(action).to(this);
    }
}