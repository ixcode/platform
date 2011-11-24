package ixcode.platform.http.protocol;

import static java.lang.String.format;

public enum IanaContentType implements ContentType {

    html("text/html"), JSON("application/json"), xhtml("application/xhtml"), XML("application/xml");


    private String identitfier;

    IanaContentType(String identitfier) {
        this.identitfier = identitfier;
    }

    public String identifier() {
        return identitfier;
    }

    public static ContentType vendorXml(final String customName) {
        return new ContentType() {
            @Override public String identifier() {
                return format("application/xml+%s.vnd");
            }
        };
    }

    public static ContentType vendorJson(final String vendortType) {
        return new ContentType() {
            @Override public String identifier() {
                return format("application/json+%s.vnd");
            }
        };
    }
}