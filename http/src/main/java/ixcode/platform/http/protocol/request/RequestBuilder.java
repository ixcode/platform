package ixcode.platform.http.protocol.request;

import ixcode.platform.http.client.Http;
import ixcode.platform.http.protocol.ContentType;
import ixcode.platform.http.protocol.ContentTypeBuilder;
import ixcode.platform.http.protocol.response.HttpResponse;
import ixcode.platform.http.representation.Hyperlink;
import ixcode.platform.http.representation.Representation;

public class RequestBuilder implements ContentTypeBuilder.ContentTypeAcceptor{


    private Http http;
    private Hyperlink hyperlink;
    private ContentType contentType;
    private String body;

    public RequestBuilder(Http http, Hyperlink hyperlink) {
        this.http = http;
        this.hyperlink = hyperlink;
    }

    public RequestContentTypeBuilder accepting() {
        return new RequestContentTypeBuilder(this);
    }

    @Override public void acceptContentTypeHeader(ContentType contentType) {
        this.contentType = contentType;
    }

    public RequestBuilder withBody(String body) {
        this.body = body;
        return this;
    }

    public Representation post() {
        return http.POST(body)
            .withHeader("Accept", contentType.identifier())
            .to(hyperlink.uri);
    }
}