package ixcode.platform.http.protocol.response;

import java.net.URI;

import static ixcode.platform.http.protocol.Header.locationHeader;
import static ixcode.platform.http.protocol.response.ResponseStatusCodes.created;
import static ixcode.platform.http.protocol.response.ResponseStatusCodes.not_found;
import static ixcode.platform.http.protocol.response.ResponseStatusCodes.ok;
import static ixcode.platform.http.protocol.response.ResponseStatusCodes.server_error;

public class ResponseStatusBuilder {
    private ResponseBuilder parent;

    public ResponseStatusBuilder(ResponseBuilder parent) {
        this.parent = parent;
    }

    public ResponseBuilder ok() {
        return parent.responseStatus(ok);
    }

    public ResponseBuilder badRequest() {
        return parent.responseStatus(ResponseStatusCodes.bad_request);
    }

    public ResponseBuilder notFound() {
        return parent.responseStatus(not_found);
    }

    public ResponseBuilder serverError() {
        return parent.responseStatus(server_error);
    }

    public ResponseBuilder created(URI location) {
        parent.header(locationHeader(location));
        return parent.responseStatus(created);
    }

    public ResponseBuilder seeOther(URI location) {
        parent.header(locationHeader(location));
        return parent.responseStatus(ResponseStatusCodes.see_other);
    }

    public ResponseBuilder custom(int code, String message) {
        return parent.responseStatus(ResponseStatusCodes.customStatus(code, message));
    }



}