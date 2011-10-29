package ixcode.platform.http.protocol.response;

public enum ResponseStatusCodes implements ResponseStatus {

    ok(200, "Ok"),
    created(201, "Created"),

    bad_request(400, "Bad request"),
    not_found(404, "Not found"),

    server_error(500, "Server error.");

    private final int code;
    private final String message;


    ResponseStatusCodes(int code, String message) {
        this.code = code;
        this.message = message;
    }


    @Override
    public int code() {
        return code;
    }

    @Override public String message() {
        return message;
    }


    public static ResponseStatus customStatus(final int code, final String message) {
        return new ResponseStatus() {
            @Override public int code() {
                return code;
            }

            @Override public String message() {
                return message;
            }
        };
    }


}