package ixcode.platform.http.protocol;

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
        target.acceptContentTypeHeader(IanaContentType.json);
    }

    public void xml() {
        target.acceptContentTypeHeader(IanaContentType.xml);
    }

    public void xhtml() {
        target.acceptContentTypeHeader(IanaContentType.xhtml);
    }


    public void jsonVendor(String vendortType) {
        target.acceptContentTypeHeader(vendorJson(vendortType));
    }

}