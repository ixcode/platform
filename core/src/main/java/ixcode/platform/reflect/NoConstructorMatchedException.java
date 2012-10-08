package ixcode.platform.reflect;

import ixcode.platform.collection.Action;
import ixcode.platform.collection.FArrayList;

import java.util.Collection;
import java.util.Set;

import static ixcode.platform.text.format.CollectionFormat.collectionToString;
import static java.lang.String.format;

public class NoConstructorMatchedException extends RuntimeException {


    public NoConstructorMatchedException(Class<?> targetClass,
                                         Set<String> parameterNames,
                                         Set<ParameterSet> parameterSets) {
        super(message(targetClass, parameterNames, parameterSets));
    }

    private static String message(Class<?> targetClass,
                                  Set<String> parameterNames,
                                  Set<ParameterSet> parameterSets) {

        return format("Could not find a constructor match!\n    Target Class [%s]\n    Input parameter names [%s].\n"
                              + "    Found these combinations: [\n %s\n    ]",
                      targetClass.getName(),
                      collectionToString(parameterNames),
                      printConstructors(targetClass, parameterSets));
    }

    private static String printConstructors(final Class<?> targetClass, Set<ParameterSet> parameterSets) {
        final StringBuilder sb = new StringBuilder();

        new FArrayList<ParameterSet>(parameterSets).apply(new Action<ParameterSet>() {
            @Override public void to(ParameterSet item, Collection<ParameterSet> tail) {
                sb.append("      ")
                  .append(printConstructor(targetClass, item));

                if (!tail.isEmpty()) {
                    sb.append("\n");
                }
            }
        });


        return sb.toString();
    }

    private static String printConstructor(Class<?> targetClass, ParameterSet parameterSet) {
        final StringBuilder sb = new StringBuilder();

        sb.append(targetClass.getSimpleName()).append("(");
        new FArrayList<ParameterSet.Parameter>(parameterSet.parameters).apply(new Action<ParameterSet.Parameter>() {
            @Override public void to(ParameterSet.Parameter item, Collection<ParameterSet.Parameter> tail) {
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


}