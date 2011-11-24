package ixcode.platform.http.protocol;

import ixcode.platform.http.protocol.*;

import static ixcode.platform.http.protocol.IanaContentType.vendorJson;

public class ContentTypeBuilder {

    public interface ContentTypeAcceptor {
        void acceptContentTypeHeader(ContentType contentType);
    }

    private ContentTypeAcceptor target;

    public ContentTypeBuilder(ContentTypeAcceptor target) {
        this.target = target;
    }

    public void json() {
        target.acceptContentTypeHeader(IanaContentType.JSON);
    }

    public void xml() {
        target.acceptContentTypeHeader(IanaContentType.XML);
    }

    public void jsonVendor(String vendortType) {
        target.acceptContentTypeHeader(vendorJson(vendortType));
    }

}