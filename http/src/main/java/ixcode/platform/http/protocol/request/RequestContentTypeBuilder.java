package ixcode.platform.http.protocol.request;

import ixcode.platform.http.protocol.ContentTypeBuilder;

public class RequestContentTypeBuilder {

    private final RequestBuilder parent;
    private final ContentTypeBuilder contentTypeBuilder;

    public RequestContentTypeBuilder(RequestBuilder parent) {
        this.parent = parent;
        contentTypeBuilder = new ContentTypeBuilder(parent);
    }

    public RequestBuilder json() {
        contentTypeBuilder.json();
        return parent;
    }
}