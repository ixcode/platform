package ixcode.platform.http.protocol.response;

import ixcode.platform.http.protocol.ContentType;
import ixcode.platform.http.protocol.ContentTypeBuilder;
import ixcode.platform.http.protocol.Header;
import ixcode.platform.http.protocol.IanaContentType;
import ixcode.platform.serialise.JsonSerialiser;
import ixcode.platform.serialise.XmlSerialiser;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ixcode.platform.http.protocol.IanaContentType.json;
import static ixcode.platform.io.IoClasspath.inputStreamFromClasspathEntry;
import static ixcode.platform.io.IoStreamHandling.closeQuietly;
import static ixcode.platform.io.IoStreamHandling.copyStream;
import static java.lang.String.format;

public class ResponseBuilder implements ContentTypeBuilder.ContentTypeAcceptor {

    private static final Logger log = Logger.getLogger(ResponseBuilder.class);

    private ResponseStatus responseStatus;
    private String responseBody = null;
    private ContentType contentType;
    private final List<Header> headers = new ArrayList<Header>();

    public final ResponseLinkBuilder linkBuilder;
    private String classpathEntry;

    private final JsonSerialiser jsonSerialiser = new JsonSerialiser();
    private final XmlSerialiser xmlSerialiser = new XmlSerialiser();

    public ResponseBuilder(ResponseLinkBuilder linkBuilder, ContentType contentType) {
        this.linkBuilder = linkBuilder;
        this.contentType = contentType;
    }

    public void translateTo(HttpServletResponse httpServletResponse) {
        httpServletResponse.setContentType(contentType.identifier());

        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");

        for (Header header : headers) {
            header.addTo(httpServletResponse);
        }

        httpServletResponse.setStatus(responseStatus.code());

        if (responseBody != null) {
            writeStringBody(httpServletResponse);
        } else if (classpathEntry != null) {
            writeClasspathBody(httpServletResponse);
            log.info("Written from the classpath @ " + classpathEntry);
        }

    }

    private void writeClasspathBody(HttpServletResponse httpServletResponse) {
        OutputStream out = null;
        InputStream in = null;
        try {
            out = httpServletResponse.getOutputStream();
            in = inputStreamFromClasspathEntry(classpathEntry);
            if (in == null) {
                throw new RuntimeException(format("Could not find classpath entry [%s] on the classpath.", classpathEntry));
            }
            copyStream(in, out);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(out);
            closeQuietly(in);
        }
    }

    private void writeStringBody(HttpServletResponse httpServletResponse) {
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

    public void bodyFromClasspath(String classpathEntry) {
        log.debug("Going to load up classpath for: " + classpathEntry);
        this.classpathEntry = classpathEntry;
    }

    public ResponseBuilder hypermedia(Map<String, Object> hypermedia) {
        if (json.equals(contentType)) {
            contentType().json();
            body(jsonSerialiser.toJson(hypermedia));
        } else {
            contentType().xml();
            body(xmlSerialiser.toXml(hypermedia));
        }
        return this;
    }
}