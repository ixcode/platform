package ixcode.platform.http.resource;


public interface ResourceMatcher<T> {

    boolean matches(T resource);

    String describeFailure();
}