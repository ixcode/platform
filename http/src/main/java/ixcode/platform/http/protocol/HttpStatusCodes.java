package ixcode.platform.http.protocol;

public enum HttpStatusCodes implements Status {

    ok(200),
    created(201),

    bad_request(400),
    not_found(404),

    error(500);

    private final int code;
    HttpStatusCodes(int code) {
        this.code = code;
    }

    @Override
    public int code() {
        return code;
    }

    public static Status customStatus(final int code) {
        return new Status() {
            @Override public int code() {
                return code;
            }
        };
    }
}