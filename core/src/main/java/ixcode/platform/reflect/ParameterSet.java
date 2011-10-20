package ixcode.platform.reflect;

import com.thoughtworks.paranamer.Paranamer;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ixcode.platform.collection.SetManipulation.intersectionOf;
import static java.lang.String.format;

public class ParameterSet {
    public final Constructor constructor;
    public final List<ParameterDefinition> parameterDefinitions = new ArrayList<ParameterDefinition>();
    public final Set<String> parameterNameSet = new HashSet<String>();

    public ParameterSet(Constructor constructor, Paranamer paranamer) {
        this.constructor = constructor;
        discoverParameters(constructor, paranamer);
    }

    private void discoverParameters(Constructor constructor, Paranamer paranamer) {
        try {
            String[] parameterNames = findParameterNames(constructor, paranamer);
            int iParameter = 0;
            for (Class<?> type : constructor.getParameterTypes()) {
                add(parameterNames[iParameter], type);
                ++iParameter;
            }

        } catch (Throwable t) {
            throw new RuntimeException(format("Could not discover parameters for Constructor [%s]", constructor));
        }
    }

    private static String[] findParameterNames(Constructor constructor, Paranamer paranamer) {
        try {
            String[] names = paranamer.lookupParameterNames(constructor, false);

            if (names.length > 0) {
                return names;
            }

            List<String> typeNames = new ArrayList<String>();
            for (Class<?> type : constructor.getParameterTypes()) {
                typeNames.add(type.getSimpleName());
            }
            return typeNames.toArray(new String[0]);
        } catch (Exception e) {
            throw new RuntimeException(format("Could not extract parameters from constructor of [%s] (%s) (See cause)", constructor.getDeclaringClass().getName(), constructor), e);
        }

    }

    void add(String name, Class<?> type) {
        parameterDefinitions.add(new ParameterDefinition(name, type));
        parameterNameSet.add(name);
    }

    public int numberOfMatchesTo(Set<String> parameterNames) {
        return intersectionOf(parameterNames, parameterNameSet).size();
    }

    public int numberOfParameters() {
        return parameterDefinitions.size();
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