package ixcode.platform.http.client;


import ixcode.platform.http.protocol.request.RequestBuilder;
import ixcode.platform.http.representation.Hyperlink;
import ixcode.platform.http.representation.Representation;
import ixcode.platform.http.representation.RepresentationDecoder;
import ixcode.platform.text.format.CollectionFormat;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static ixcode.platform.http.protocol.UriFactory.uri;
import static ixcode.platform.http.protocol.response.ResponseStatusCodes.codeToStatus;
import static ixcode.platform.io.StreamHandling.closeQuietly;
import static ixcode.platform.io.StreamHandling.readFully;
import static ixcode.platform.text.format.CollectionFormat.collectionToString;

public class Http {

    private static final Logger log = Logger.getLogger(Http.class);

    public PostRequest POST(String body) {
        return new PostRequest(body);
    }

    public GetRequest GET(Class<?> entityClass) {
        return new GetRequest(entityClass);
    }

    public GetRequest GET() {
        return new GetRequest(null);
    }

    public RequestBuilder makeRequestTo(Hyperlink hyperlink) {
        return new RequestBuilder(this, hyperlink);
    }

    public static class GetRequest extends BaseRequest<GetRequest> {

        private Class<?> entityClass;

        public GetRequest(Class<?> entityClass) {
            this.entityClass = entityClass;
        }

        public Representation from(String uriString) {
            return from(uri(uriString));
        }

        public Representation from(URI uri) {
            try {
                if (log.isInfoEnabled()) {
                    log.info("\nGET " + uri.toURL().toExternalForm() + " HTTP/1.1");
                    logHeaders();
                }

                HttpURLConnection urlConnection = (HttpURLConnection) uri.toURL().openConnection();
                urlConnection.setInstanceFollowRedirects(true);
                addHeadersTo(urlConnection);
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

                Map<String, List<String>> httpHeaders = urlConnection.getHeaderFields();

                return new RepresentationDecoder(entityClass).representationFrom(codeToStatus(responseCode), responseBody, httpHeaders);

            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static class PostRequest extends BaseRequest<PostRequest> {


        public PostRequest(String body) {
            this.body = body;
        }


        public Representation to(URI uri) {
            try {
                if (log.isInfoEnabled()) {
                    log.info("\nPOST " + uri.toURL().toExternalForm() + " HTTP/1.1");
                    logHeaders();
                    if (body != null) {
                        log.info(body);
                    }
                }
                HttpURLConnection urlConnection = (HttpURLConnection) uri.toURL().openConnection();
                urlConnection.setInstanceFollowRedirects(false);
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
                logHeaders(urlConnection.getHeaderFields());

                if (log.isDebugEnabled() && (responseBody != null) && (responseBody.length() >0)) {
                    log.debug(responseBody);
                }

                Map<String, List<String>> httpHeaders = urlConnection.getHeaderFields();

                return new RepresentationDecoder(null).representationFrom(codeToStatus(responseCode), responseBody, httpHeaders);

            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private void logHeaders(Map<String, List<String>> headerFields) {
            if (!log.isInfoEnabled()) {
                return;
            }

            for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
                log.info(entry.getKey() + ": " + collectionToString(entry.getValue()));
            }
        }


    }

    public static abstract class BaseRequest<T extends BaseRequest> {

        protected String body;
        protected Map<String, String> headers = new LinkedHashMap<String, String>();

        public T withHeader(String name, String value) {
            headers.put(name, value);
            return (T)this;
        }


        protected void logHeaders() {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                log.info(entry.getKey() + ": " + entry.getValue());
            }
        }

        protected void writeBodyTo(HttpURLConnection urlConnection) {
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


        protected void addHeadersTo(HttpURLConnection urlConnection) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
    }
}