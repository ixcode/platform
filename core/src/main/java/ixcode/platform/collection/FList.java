package ixcode.platform.collection;

import java.util.*;

public interface FList<T> extends List<T> {
    void apply(Action<T> action);
}