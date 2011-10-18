package ixcode.platform.text.format;

import java.net.*;

public class UriFormat extends AbstractFormat {

    @Override
    public URI parseString(String source) {
        try {
            return new URI(source);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Could not parse URI (see cause)" + source, e);
        }
    }

    @Override
    public String format(Object source) {
        URI uri = (URI)source;
        try {
            return uri.toURL().toExternalForm();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not format URI (see cause)" + source, e);
        }
    }
}