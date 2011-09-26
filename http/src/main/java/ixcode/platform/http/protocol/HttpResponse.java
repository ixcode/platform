package ixcode.platform.http.protocol;

import javax.servlet.http.*;
import java.io.*;

public class HttpResponse {
    private int responseCode;
    private String responseBody;

    public void translateTo(HttpServletResponse httpServletResponse) {
        httpServletResponse.setStatus(responseCode);
        try {
            httpServletResponse.getWriter().print(responseBody);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
}