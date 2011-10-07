package ixcode.platform.http.protocol;

import javax.servlet.http.*;
import java.io.*;

public class ResponseBuilder {
    private Status status;
    private String responseBody;
    private ContentType contentType;

    public void translateTo(HttpServletResponse httpServletResponse) {
        httpServletResponse.setStatus(status.code());
        try {
            httpServletResponse.getWriter().print(responseBody);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseBuilder statusCode(Status status) {
        this.status = this.status;
        return this;
    }

    public ResponseBuilder body(String responseBody) {
        this.responseBody = responseBody;
        return this;
    }

    public ResponseBuilder contentType(ContentType contentType) {
        this.contentType = contentType;
        return this;
    }

}