package ixcode.platform.reflect;

import java.util.*;

import static ixcode.platform.text.format.CollectionFormat.collectionToString;
import static java.lang.String.format;

public class NoConstructorMatchedException extends RuntimeException {


    public NoConstructorMatchedException(Set<String> parameterNames) {
        super(format("Could not find a constructor matching parameter names [%s]", collectionToString(parameterNames)));
    }


}