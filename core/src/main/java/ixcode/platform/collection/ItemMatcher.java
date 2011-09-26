package ixcode.platform.collection;

public interface ItemMatcher<S, T> {
    boolean matches(S sourceItem, T targetItem);
}