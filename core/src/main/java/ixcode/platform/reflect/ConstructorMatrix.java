package ixcode.platform.reflect;

import com.thoughtworks.paranamer.*;
import ixcode.platform.collection.*;

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
        ParameterSet parameterSet = new ParameterSet(constructor);

        String[] parameterNames = paranamer.lookupParameterNames(constructor);
        int iParameter = 0;
        for (Class<?> type : constructor.getParameterTypes()) {
            parameterSet.add(parameterNames[iParameter], type, constructor);
            ++iParameter;
        }

        return parameterSet;
    }

    public ParameterSet findMostSpecificMatchTo(Set<String> propertyNames) {
        int currentMatchCount = -1;
        ParameterSet mostSpecificParameterSet = null;

        for (ParameterSet parameterSet : matrix.keySet()) {
            int matchCount = parameterSet.numberOfMatchesTo(propertyNames);
            if (matchCount > currentMatchCount) {
                currentMatchCount = matchCount;
                mostSpecificParameterSet = parameterSet;
            }
        }

        return mostSpecificParameterSet;
    }

    public static class ParameterSet {
        public final Constructor constructor;
        public final Set<ParameterDefinition> parameterDefinitions = new HashSet<ParameterDefinition>();

        public ParameterSet(Constructor constructor) {
            this.constructor = constructor;
        }

        public void add(String name, Class<?> type, Constructor constructor) {
            parameterDefinitions.add(new ParameterDefinition(name, type));
        }

        public int numberOfMatchesTo(Set<String> propertyNames) {
            Set<ParameterDefinition> intersection = SetManipulation.intersectionOf(propertyNames, parameterDefinitions, new ItemMatcher<String, ParameterDefinition>() {

                public boolean matches(String propertyName, ParameterDefinition parameterDefinition) {
                    return propertyName.equals(parameterDefinition.name);
                }
            });

            return intersection.size();
        }

    }

    public static class ParameterDefinition {
        public final String name;
        public final Class<?> type;

        private ParameterDefinition(String name, Class<?> type) {
            this.name = name;
            this.type = type;
        }
    }
}