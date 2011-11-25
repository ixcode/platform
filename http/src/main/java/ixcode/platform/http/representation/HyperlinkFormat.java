package ixcode.platform.http.representation;

import ixcode.platform.text.format.Format;

import static ixcode.platform.http.protocol.UriFactory.uri;
import static ixcode.platform.http.representation.Hyperlink.hyperlinkTo;

public class HyperlinkFormat implements Format {
    @Override public <T> T parseString(String source) {
        return (T)hyperlinkTo(uri(source));
    }

    @Override public String format(Object source) {
        return ((Hyperlink)source).toUriExternalForm();
    }
}