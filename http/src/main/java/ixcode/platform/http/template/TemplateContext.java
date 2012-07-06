package ixcode.platform.http.template;

import java.util.HashMap;
import java.util.Map;

public class TemplateContext {
    private Map<String, Object> context = new HashMap<String, Object>();

    public static TemplateContext templateContext() {
        return new TemplateContext();
    }

    public TemplateValueBuilder key(String key) {
        return new TemplateValueBuilder(this, key);
    }

    public TemplateContext fromMap(Map<String, Object> data) {
        this.context = data;
        return this;
    }

    public static class TemplateValueBuilder {
        private final TemplateContext parent;
        private final String key;

        public TemplateValueBuilder(TemplateContext parent, String key) {
            this.parent = parent;
            this.key = key;
        }

        public TemplateContext value(Object value) {
            parent.setValue(key, value);
            return parent;
        }
    }

    public Map<String, Object> asMap() {
        return context;
    }

    private void setValue(String key, Object value) {
        context.put(key, value);
    }
}