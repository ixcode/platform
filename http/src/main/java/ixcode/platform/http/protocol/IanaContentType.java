package ixcode.platform.http.protocol;

import static java.lang.String.format;

public enum IanaContentType implements ContentType {

    html("text/html"), JSON("application/json"), xhtml("application/xhtml"), xml("application/xml");


    private String identitfier;

    IanaContentType(String identitfier) {
        this.identitfier = identitfier;
    }

    public String getIdentifier() {
        return identitfier;
    }

    public static ContentType vendorXml(final String customName) {
        return new ContentType() {
            @Override public String getIdentifier() {
                return format("application/xml+%s.vnd");
            }
        };
    }

    public static ContentType vendorJson(final String vendortType) {
        return new ContentType() {
            @Override public String getIdentifier() {
                return format("application/json+%s.vnd");
            }
        };
    }
}