package ixcode.platform.http.protocol;

import static java.lang.String.format;

public enum IanaContentType implements ContentType {

    html("text/html"), json("application/json"), xhtml("application/xhtml"), xml("application/xml");


    private String identitfier;

    IanaContentType(String identitfier) {
        this.identitfier = identitfier;
    }

    public String getIdentifier() {
        return identitfier;
    }

    public static ContentType customXmlType(final String customName) {
        return new ContentType() {
            @Override public String getIdentifier() {
                return format("application/xml+%s.vnd");
            }
        };
    }

}