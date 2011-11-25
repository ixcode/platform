package ixcode.platform.http.protocol.response;

import ixcode.platform.http.protocol.ContentType;
import ixcode.platform.http.protocol.ContentTypeBuilder;
import ixcode.platform.http.protocol.Header;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResponseBuilder implements ContentTypeBuilder.ContentTypeAcceptor {
    private ResponseStatus responseStatus;
    private String responseBody = "";
    private ContentType contentType;
    private final List<Header> headers = new ArrayList<Header>();

    public void translateTo(HttpServletResponse httpServletResponse) {
        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");

        for (Header header : headers) {
            header.addTo(httpServletResponse);
        }

        httpServletResponse.setStatus(responseStatus.code());

        try {
            httpServletResponse.getWriter().print(responseBody);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public ResponseStatusBuilder status() {
        return new ResponseStatusBuilder(this);
    }

    public ResponseContentTypeBuilder contentType() {
        return new ResponseContentTypeBuilder(this);
    }

    public ResponseBuilder body(String responseBody) {
        this.responseBody = responseBody;
        return this;
    }

    public ResponseBuilder header(Header header) {
        this.headers.add(header);
        return this;
    }

    ResponseBuilder responseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
        return this;
    }

    ResponseBuilder contentTypeHeader(ContentType contentType) {
        this.contentType = contentType;
        return this;
    }

    @Override public void acceptContentTypeHeader(ContentType contentType) {
        contentTypeHeader(contentType);
    }
}