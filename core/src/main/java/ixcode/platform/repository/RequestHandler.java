package ixcode.platform.repository;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RequestHandler {

    String[] objectTags();

    Class<?> handler();
}
