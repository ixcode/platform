package ixcode.platform.reflect;

import com.thoughtworks.paranamer.Paranamer;
import ixcode.platform.collection.Action;
import ixcode.platform.collection.FArrayList;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static ixcode.platform.collection.SetManipulation.intersectionOf;
import static java.lang.String.format;

public class ParameterSet {
    public final Constructor constructor;
    public final List<Parameter> parameters = new ArrayList<Parameter>();
    public final Set<String> parameterNameSet = new LinkedHashSet<String>();

    public ParameterSet(Constructor constructor, Paranamer paranamer) {
        this.constructor = constructor;
        discoverParameters(constructor, paranamer);
    }

    public static String printConstructor(ParameterSet parameterSet) {
        Class<?> targetClass = parameterSet.constructor.getDeclaringClass();

        final StringBuilder sb = new StringBuilder();

        sb.append(targetClass.getSimpleName()).append("(");
        new FArrayList<Parameter>(parameterSet.parameters).apply(new Action<Parameter>() {
            @Override public void to(Parameter item, Collection<Parameter> tail) {
                sb.append(item.type.getSimpleName()).append(" ");
                sb.append(item.name);

                if (tail.size() > 0) {
                    sb.append(", ");
                }
            }
        });

        sb.append(")");
        return sb.toString();
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
        parameters.add(new Parameter(name, type));
        parameterNameSet.add(name);
    }

    public int numberOfMatchesTo(Set<String> parameterNames) {
        return intersectionOf(parameterNames, parameterNameSet).size();
    }

    public int numberOfParameters() {
        return parameters.size();
    }

    public static class Parameter {
        public final String name;
        public final Class<?> type;

        private Parameter(String name, Class<?> type) {
            this.name = name;
            this.type = type;
        }
    }
}