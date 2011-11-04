package ixcode.platform.http.server.resource.path;

import ixcode.platform.http.representation.Hyperlink;
import ixcode.platform.text.regex.MatcherPrinter;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ixcode.platform.collection.CollectionPrinter.printCollection;
import static ixcode.platform.http.server.resource.path.UriTemplateMatch.noMatch;
import static java.util.regex.Pattern.compile;

public class UriTemplate {

    private static final Logger log = Logger.getLogger(UriTemplate.class);

    private final String uriRoot;
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
        return new UriTemplate(uriRoot, compile(pathWithParametersSubstituted), parameterNames);
    }

    private UriTemplate(String uriRoot, Pattern regexPattern, List<String> parameterNames) {
        this.uriRoot = uriRoot;
        this.regexPattern = regexPattern;
        this.parameterNames = parameterNames;
    }

    public boolean matches(Map<String, String> properties) {
        return parameterNames.containsAll(properties.keySet());
    }

    public Hyperlink hyperlinkFrom(Map<String, String> properties) {
        return null;
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