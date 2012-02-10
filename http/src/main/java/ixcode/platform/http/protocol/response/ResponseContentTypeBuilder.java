package ixcode.platform.http.protocol.response;

import ixcode.platform.http.protocol.ContentTypeBuilder;

public class ResponseContentTypeBuilder {

    private final ResponseBuilder parent;
    private final ContentTypeBuilder contentTypeBuilder;

    public ResponseContentTypeBuilder(ResponseBuilder parent) {
        this.parent = parent;
        contentTypeBuilder = new ContentTypeBuilder(parent);
    }


    public ResponseBuilder json() {
        contentTypeBuilder.json();
        return parent;
    }

    public ResponseBuilder xhtml() {
        contentTypeBuilder.xhtml();
        return parent;
    }
}