package ixcode.platform.text;

import java.net.*;

public class UriFormat extends AbstractFormat<URI> {
    @Override
    public URI parseString(String source) {
        try {
            return new URI(source);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Could not parse URI (see cause)" + source, e);
        }
    }

    @Override
    public String format(URI source) {
        try {
            return source.toURL().toExternalForm();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not format URI (see cause)" + source, e);
        }
    }
}