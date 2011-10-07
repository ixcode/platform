package ixcode.platform.collection;

import java.util.*;

public interface Action<T> {

    void to(T item, Collection<T> tail);

}