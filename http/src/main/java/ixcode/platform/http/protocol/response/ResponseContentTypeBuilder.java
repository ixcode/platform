package ixcode.platform.http.protocol.response;

import ixcode.platform.http.protocol.*;

import static ixcode.platform.http.protocol.IanaContentType.vendorJson;

public class ResponseContentTypeBuilder {

    private ResponseBuilder parent;

    public ResponseContentTypeBuilder(ResponseBuilder parent) {
        this.parent = parent;
    }

    public ResponseBuilder json() {
        parent.contentTypeHeader(IanaContentType.JSON);
        return parent;
    }

    public ResponseBuilder xml() {
        parent.contentTypeHeader(IanaContentType.XML);
        return parent;
    }

    public ResponseBuilder jsonVendor(String vendortType) {
        parent.contentTypeHeader(vendorJson(vendortType));
        return parent;
    }

}