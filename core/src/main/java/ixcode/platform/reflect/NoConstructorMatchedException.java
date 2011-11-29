package ixcode.platform.reflect;

import java.util.*;

import static ixcode.platform.text.format.CollectionFormat.collectionToString;
import static java.lang.String.format;

public class NoConstructorMatchedException extends RuntimeException {


    public NoConstructorMatchedException(Class<?> targetClass, Set<String> parameterNames) {
        super(format("Could not find a constructor on [%s] matching parameter names [%s]", targetClass.getName(), collectionToString(parameterNames)));
    }


}