package ixcode.platform.http.server.resource;

import java.util.LinkedHashMap;
import java.util.Map;

public class RedirectionParameters {

    private Map<String, Object> parameters = new LinkedHashMap<String, Object>();

    public RedirectionParameters() {
        this.parameters = parameters;
    }

    public RedirectionParameters add(String key, Object value) {
        parameters.put(key, value);
        return this;
    }

    public String toUrlSection() {
        StringBuilder sb = new StringBuilder();
        for (String key : parameters.keySet()) {
            sb.append("/").append(parameters.get(key));
        }
        return sb.toString();
    }
}