package ixcode.platform.collection;

import java.util.*;

public class FArrayList<T> extends ArrayList<T> implements FList<T> {

    @Override
    public void apply(Action<T> action) {
        Apply.the(action).to(this);
    }
}