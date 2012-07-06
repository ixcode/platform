package ixcode.platform.http.server.resource;

import java.io.File;
import java.util.Map;

class TemplateConfig {
    public final Map<String, Object> get;
    public final Map<String, Object> post;


    TemplateConfig(Map<String, Object> get,
                   Map<String, Object> post) {
        this.get = get;
        this.post = post;
    }

    public TemplatedPageEntry toEntry(String path, String templateName, File sourceFile) {
        return new TemplatedPageEntry(path,
                                      templateName,
                                      sourceFile,
                                      (Map<String, Object>)get.get("data"),
                                      (String)post.get("redirect-to"));
    }
}