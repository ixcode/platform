package ixcode.platform.reflect;

import com.thoughtworks.paranamer.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * @todo Doesn't work with non-static inner classes
 */
public class ConstructorMatrix {

    private static Paranamer paranamer = new AdaptiveParanamer();

    private Map<ParameterSet, Constructor> matrix = new LinkedHashMap<ParameterSet, Constructor>();
    private final Class<?> targetClass;

    public ConstructorMatrix(Class<?> targetClass) {
        this.targetClass = targetClass;
        for (Constructor constructor : targetClass.getDeclaredConstructors()) {
            matrix.put(parametersFrom(constructor), constructor);
        }
    }

    private ParameterSet parametersFrom(Constructor constructor) {
        return new ParameterSet(constructor, paranamer);
    }

    public ParameterSet findStringConstructor() {
        for (ParameterSet parameterSet : matrix.keySet()) {
            if (parameterSet.isStringConstructor()) {
                return parameterSet;
            }
        }

        throw new NoConstructorMatchedException(String.class, Collections.<String>emptySet(), matrix.keySet());
    }

    public ParameterSet findMostSpecificMatchTo(Set<String> parameterNames) {
        int currentMatchCount = -1;
        ParameterSet mostSpecificParameterSet = null;

        for (ParameterSet parameterSet : matrix.keySet()) {
            int matchCount = parameterSet.numberOfMatchesTo(parameterNames);
            if (matchCount == parameterSet.numberOfParameters()
                    && matchCount > currentMatchCount) {
                currentMatchCount = matchCount;
                mostSpecificParameterSet = parameterSet;
            }
        }

        if (mostSpecificParameterSet == null) {
            throw new NoConstructorMatchedException(targetClass, parameterNames, matrix.keySet());
        }

        return mostSpecificParameterSet;
    }


}