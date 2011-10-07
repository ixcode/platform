package ixcode.platform.collection;

public interface Appliable<T> {

    void apply(Action<T> action);

}