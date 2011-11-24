package ixcode.platform.http.protocol.request;

import ixcode.platform.http.protocol.ContentType;
import ixcode.platform.http.protocol.ContentTypeBuilder;

public class RequestBuilder implements ContentTypeBuilder.ContentTypeAcceptor{

    public RequestContentTypeBuilder accepts() {
        return new RequestContentTypeBuilder(this);
    }

    @Override public void acceptContentTypeHeader(ContentType contentType) {

    }
}