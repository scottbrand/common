package test.io.github.scottbrand.webservice;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.osgi.service.component.annotations.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JacksonProvider<T> implements MessageBodyReader<T>, MessageBodyWriter<T>
{

    private ObjectMapper mapper;





    public JacksonProvider()
    {
        mapper = new ObjectMapper();
    }






    @Override
    public long getSize(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return -1;
    }





    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return true;
    }





    @Override
    public void writeTo(T object, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException
    {
        try (OutputStream stream = entityStream)
        {
            entityStream.write(mapper.writeValueAsString(object).getBytes("utf-8"));
            entityStream.flush();
        }
    }





    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return true;
    }





    @Override
    public T readFrom(Class<T> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException
    {
        try (InputStreamReader reader = new InputStreamReader(entityStream, "UTF-8"))
        {
            return mapper.readValue(reader, type);
        }
    }
}
