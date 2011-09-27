package ixcode.platform.reflect;

import ixcode.platform.text.*;

import java.util.*;

import static ixcode.platform.text.CollectionFormat.collectionToString;
import static java.lang.String.format;

public class NoConstructorMatchedException extends RuntimeException {


    public NoConstructorMatchedException(Set<String> parameterNames) {
        super(format("Could not find a constructor matching parameter names [%s]", collectionToString(parameterNames)));
    }


}