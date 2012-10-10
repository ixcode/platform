package ixcode.platform.jersey;

import com.sun.jersey.core.provider.AbstractMessageReaderWriterProvider;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;

@Provider
@Consumes(APPLICATION_FORM_URLENCODED  + ";charset=utf-8")
public class PojoFormProvider extends AbstractMessageReaderWriterProvider<Object> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return false; // Always going to be false
    }

    @Override
    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations,
                           MediaType mediaType,
                           MultivaluedMap<String, String> httpHeaders,
                           InputStream entityStream) throws IOException, WebApplicationException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(entityStream, "UTF-8"));

        StringBuilder entityBody = new StringBuilder();

        while (reader.ready()) {
            entityBody.append(reader.readLine()).append("\n");
        }

        System.out.println("Here is my entity body:\n" + entityBody + "\n");

        return null;
    }

    @Override
    public void writeTo(Object o, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {

    }
}