package ixcode.platform.http.server.resource;

public interface ResourceHyperlinkBuilder {
    <T extends UriTemplateGenerator> T linkTo(Class<T> templateClass);
}