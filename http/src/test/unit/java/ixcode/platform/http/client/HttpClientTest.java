package ixcode.platform.http.client;

import ixcode.platform.test.SystemTest;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.net.URL;

import static ixcode.platform.http.protocol.UriFactory.uri;
import static ixcode.platform.io.IoStreamHandling.readFully;

@SystemTest
public class HttpClientTest {

    private static final Logger log = Logger.getLogger(HttpClientTest.class);

    @Test
    public void can_get_using_relative_uri_and_hostname() throws Exception {

        Http http = new Http();

        URL url = new URL(uri("http://localhost:8080/").toURL(), "sites");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setInstanceFollowRedirects(true);

        urlConnection.connect();
        int responseCode = urlConnection.getResponseCode();
        String responseBody = null;
        if (responseCode < 200 || responseCode > 399) {
            responseBody = readFully(urlConnection.getErrorStream(), "UTF-8");
        } else {
            responseBody = readFully(urlConnection.getInputStream(), "UTF-8");
        }

        log.info("HTTP/1.1 " + responseCode + " " + urlConnection.getResponseMessage());

        if (log.isDebugEnabled()) {
            log.debug(responseBody);
        }


    }

}