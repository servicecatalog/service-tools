/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: May 9, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.interfaces.exceptions.InternalException;
import org.oscm.common.interfaces.exceptions.ValidationException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * Provider class for message body writer and reader with gson.
 * 
 * @author miethaner
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GsonMessageProvider implements MessageBodyWriter<Representation>,
        MessageBodyReader<Representation> {

    public static final Charset CHARSET = StandardCharsets.UTF_8;
    public static final String FORMAT_DATE = "yyyy-MM-dd'T'HH:mm:ssXXX";

    @Override
    public boolean isReadable(Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public Representation readFrom(Class<Representation> type, Type genericType,
            Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, String> httpHeaders,
            InputStream entityStream) throws IOException {

        InputStreamReader reader = new InputStreamReader(entityStream, CHARSET);

        try {
            Gson gson = new GsonBuilder().setDateFormat(FORMAT_DATE).create();
            return gson.fromJson(reader, genericType);

        } catch (JsonSyntaxException e) {
            ValidationException ve = new ValidationException(
                    Messages.JSON_FORMAT, null, e);

            throw new ExceptionMapper().toWebException(ve);

        } finally {
            reader.close();
        }
    }

    @Override
    public long getSize(Representation rep, Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public void writeTo(Representation rep, Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders,
            OutputStream entityStream)
            throws IOException, WebApplicationException {

        OutputStreamWriter writer = new OutputStreamWriter(entityStream,
                CHARSET);

        try {
            GsonBuilder builder = new GsonBuilder();
            builder.setDateFormat(FORMAT_DATE);

            if (rep.getVersion() != null) {
                builder.setVersion(rep.getVersion().intValue());
            }

            Gson gson = builder.create();
            gson.toJson(rep, genericType, writer);

        } catch (JsonSyntaxException e) {
            InternalException ie = new InternalException(Messages.JSON_FORMAT,
                    e);

            throw new ExceptionMapper().toWebException(ie);
        } finally {
            writer.close();
        }

    }

}
