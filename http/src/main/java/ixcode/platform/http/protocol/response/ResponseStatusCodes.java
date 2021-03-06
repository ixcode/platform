package ixcode.platform.http.protocol.response;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

public enum ResponseStatusCodes implements ResponseStatus {

    ok(200, "Ok"),
    created(201, "Created"),

    see_other(303, "See other"),

    bad_request(400, "Bad request"),
    not_found(404, "Not found"),
    method_not_supported(405, "Method not supported"),
    gone(410, "Gone"),

    server_error(500, "Server error");


    private final int code;
    private final String message;

    private static final Map<Integer, ResponseStatus> CUSTOM_STATUS_LOOKUP = new HashMap<Integer, ResponseStatus>();

    private ResponseStatusCodes(int code, String message) {
        this.code = code;
        this.message = message;
    }


    public static ResponseStatus codeToStatus(int responseCode) {
        for (ResponseStatus status : ResponseStatusCodes.values()) {
            if (status.code() == responseCode) {
                return status;
            }
        }

        return lookupCustomStatus(responseCode);
    }



    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

    public boolean isError() {
        return isResponseAnError(this);
    }


    public static ResponseStatus customStatus(final int code, final String message) {
        ResponseStatus status = new ResponseStatus() {
            public int code() {
                return code;
            }

            public String message() {
                return message;
            }

            public boolean isError() {
                return isResponseAnError(this);
            }
        };
        registerStatus(status);
        return status;
    }

    private static void registerStatus(ResponseStatus status) {
        CUSTOM_STATUS_LOOKUP.put(status.code(), status);
    }

    private static ResponseStatus lookupCustomStatus(Integer code) {
        if (CUSTOM_STATUS_LOOKUP.containsKey(code)) {
            return CUSTOM_STATUS_LOOKUP.get(code);
        }
        throw new RuntimeException("Could not find custom status for code " + code);
    }


    private static boolean isResponseAnError(ResponseStatus responseStatus) {
        return responseStatus.code() < 200 || responseStatus.code() > 299;
    }

    public String toString() {
        return format("%s %s", code, message);
    }
}