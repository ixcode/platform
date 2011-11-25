package ixcode.platform.http.server.resource.path;

import ixcode.platform.collection.Action;
import ixcode.platform.collection.FArrayList;
import ixcode.platform.collection.FList;
import ixcode.platform.http.representation.Hyperlink;
import ixcode.platform.text.regex.MatcherPrinter;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ixcode.platform.collection.CollectionPrinter.printCollection;
import static ixcode.platform.http.protocol.UriFactory.uri;
import static ixcode.platform.http.representation.Hyperlink.hyperlinkTo;
import static ixcode.platform.http.server.resource.path.UriTemplateMatch.noMatch;
import static java.lang.String.format;
import static java.util.regex.Pattern.compile;

public class UriTemplate {

    private static final Logger log = Logger.getLogger(UriTemplate.class);

    private final String uriRoot;
    private String path;
    private final Pattern regexPattern;
    private List<String> parameterNames;

    public static UriTemplate uriTemplateFrom(String uriRoot, String path) {

        List<String> parameterNames = new ArrayList<String>();

        Pattern parameterPattern = Pattern.compile("\\{\\w*\\}");
        Matcher matcher = parameterPattern.matcher(path);
        while (matcher.find()) {
            parameterNames.add(removeCurlyBraces(matcher.group()));
        }

        String pathWithParametersSubstituted = matcher.replaceAll("([^./]*)");
        return new UriTemplate(uriRoot, path, compile(pathWithParametersSubstituted), parameterNames);
    }

    private UriTemplate(String uriRoot, String path, Pattern regexPattern, List<String> parameterNames) {
        this.uriRoot = uriRoot;
        this.path = path;
        this.regexPattern = regexPattern;
        this.parameterNames = parameterNames;
    }

    public boolean matches(Map<String, String> properties) {
        return parameterNames.containsAll(properties.keySet());
    }

    public Hyperlink hyperlinkFrom(Map<String, String> uriParameters,
                                   Map<String, String> queryParameters,
                                   String path,
                                   String relation) {

        String substitutedPath = substituteUriParameters(uriParameters);
        String queryString = buildQueryString(queryParameters);

        return hyperlinkTo(uri(uriRoot + substitutedPath + path + queryString), relation);
    }


    private String substituteUriParameters(Map<String, String> uriParameters) {
        String substitutedPath = path;
        for (String parameterName : parameterNames) {
            String parameter = format("{%s}", parameterName);
            String parameterValue = uriParameters.get(parameterName);
            if (parameterValue == null) {
                throw new RuntimeException("Null Parameter value supplied for " + parameter);
            }
            substitutedPath = substitutedPath.replace(parameter, parameterValue);
        } return substitutedPath;
    }

    private String buildQueryString(final Map<String, String> queryParameters) {
        if (queryParameters.size() == 0) {
            return "";
        }

        final StringBuilder sb = new StringBuilder().append("?");

        FList<String> parameterNames = new FArrayList<String>(queryParameters.keySet());
        parameterNames.apply(new Action<String>() {
            @Override public void to(String key, Collection<String> tail) {
                sb.append(key).append("=").append(queryParameters.get(key));
                if (!tail.isEmpty()) {
                    sb.append("&");
                }
            }
        });

        return sb.toString();
    }


    public UriTemplateMatch match(String path) {
        Matcher matcher = regexPattern.matcher(path);

        if (log.isDebugEnabled()) {
            log.debug("\nMatching     : " + path + "\n" + "Parameters   : " + printCollection(parameterNames) + "\n" + new MatcherPrinter(matcher).toString());
        }

        if (!matcher.matches()) {
            return noMatch();
        }

        Map<String, String> parameters = new HashMap<String, String>();
        for (int i = 0; i < parameterNames.size(); ++i) {
            parameters.put(parameterNames.get(i), matcher.group(i + 1));
        }
        int matchLevel = (path.split("/").length - 1) - parameters.size();
        return new UriTemplateMatch(matchLevel, parameters);
    }

    private static String removeCurlyBraces(String parameter) {
        return parameter.replaceAll("[\\{\\}]", "");
    }


}