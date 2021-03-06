package ixcode.platform.repository;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Resource {

    String collectionName() default "";

    boolean previewList() default false;

    boolean queryable() default true;
}