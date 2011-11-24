package ixcode.platform.http.client;


import ixcode.platform.http.protocol.request.RequestBuilder;
import ixcode.platform.http.representation.*;
import org.apache.log4j.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static ixcode.platform.http.protocol.UriFactory.*;
import static ixcode.platform.http.protocol.response.ResponseStatusCodes.codeToStatus;
import static ixcode.platform.io.StreamHandling.*;

public class Http {

    private static final Logger log = Logger.getLogger(Http.class);

    public PostRequest POST(String body) {
        return new PostRequest(body);
    }

    public GetRepresentationRequest GET(Class<?> entityClass) {
        return new GetRepresentationRequest(entityClass);
    }

    public GetRepresentationRequest GET() {
        return new GetRepresentationRequest(null);
    }

    public RequestBuilder makeRequestTo(Hyperlink hyperlink) {
        return new RequestBuilder(this, hyperlink);
    }

    public static class GetRepresentationRequest {

        private Class<?> entityClass;

        public GetRepresentationRequest(Class<?> entityClass) {
            this.entityClass = entityClass;
        }

        public Representation from(String uriString) {
            return from(uri(uriString));
        }

        public Representation from(URI uri) {
            try {
                log.info("GET " + uri.toURL().toExternalForm() + " HTTP/1.1");
                HttpURLConnection urlConnection = (HttpURLConnection) uri.toURL().openConnection();
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
                    log.debug("\n" + responseBody);
                }

                Map<String, List<String>> httpHeaders = urlConnection.getHeaderFields();

                return new RepresentationDecoder(entityClass).representationFrom(codeToStatus(responseCode), responseBody, httpHeaders);

            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class PostRequest {

        private String body;
        private Map<String, String> headers = new LinkedHashMap<String, String>();


        public PostRequest(String body) {
            this.body = body;
        }

        public PostRequest withHeader(String name, String value) {
            headers.put(name, value);
            return this;
        }

        public Representation to(URI uri) {
             try {
                if (log.isInfoEnabled()) {
                    log.info("POST " + uri.toURL().toExternalForm() + " HTTP/1.1");
                    logHeaders(headers);
                    log.info(body);
                }
                HttpURLConnection urlConnection = (HttpURLConnection) uri.toURL().openConnection();
                 urlConnection.setDoOutput(true);
                addHeadersTo(urlConnection);
                writeBodyTo(urlConnection);
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
                    log.debug("\n" + responseBody);
                }

                Map<String, List<String>> httpHeaders = urlConnection.getHeaderFields();

                return new RepresentationDecoder(null).representationFrom(codeToStatus(responseCode), responseBody, httpHeaders);

            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private void logHeaders(Map<String, String> headers) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                log.info(entry.getKey() + ": " + entry.getValue());
            }
        }

        private void writeBodyTo(HttpURLConnection urlConnection) {
            PrintWriter out = null;
            try {
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream())));
                out.append(this.body);
            } catch (IOException e) {
                throw new RuntimeException("Could not print body to output stream (See Cause)", e);
            } finally {
                closeQuietly(out);
            }
        }

        private void addHeadersTo(HttpURLConnection urlConnection) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

    }
}