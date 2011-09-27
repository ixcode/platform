package ixcode.platform.reflect;

import com.thoughtworks.paranamer.*;

import java.lang.reflect.*;
import java.util.*;

public class ConstructorMatrix {

    private static Paranamer paranamer = new AdaptiveParanamer();

    private Map<ParameterSet, Constructor> matrix = new HashMap<ParameterSet, Constructor>();

    public ConstructorMatrix(Class<?> targetClass) {
        for (Constructor constructor : targetClass.getDeclaredConstructors()) {
            matrix.put(parametersFrom(constructor), constructor);
        }
    }

    private ParameterSet parametersFrom(Constructor constructor) {
        return new ParameterSet(constructor, paranamer);
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
            throw new NoConstructorMatchedException(parameterNames);
        }

        return mostSpecificParameterSet;
    }

}