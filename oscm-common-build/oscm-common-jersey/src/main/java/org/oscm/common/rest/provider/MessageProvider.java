/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: May 9, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.oscm.common.interfaces.data.Event;
import org.oscm.common.interfaces.data.VersionedEntity;
import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.interfaces.exceptions.InternalException;
import org.oscm.common.interfaces.exceptions.ValidationException;
import org.oscm.common.interfaces.keys.ActivityKey;
import org.oscm.common.interfaces.keys.VersionKey;
import org.oscm.common.rest.ServiceRequestContext;
import org.oscm.common.util.ConfigurationManager;
import org.oscm.common.util.serializer.ActivitySerializer;
import org.oscm.common.util.serializer.EventSerializer;
import org.oscm.common.util.serializer.VersionSerializer;

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
public class MessageProvider implements MessageBodyReader<VersionedEntity>,
        MessageBodyWriter<VersionedEntity> {

    @Inject
    private ServiceRequestContext context;

    public void setContext(ServiceRequestContext context) {
        this.context = context;
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public VersionedEntity readFrom(Class<VersionedEntity> type,
            Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, String> httpHeaders,
            InputStream entityStream) throws IOException {

        InputStreamReader reader = new InputStreamReader(entityStream,
                ConfigurationManager.CHARSET);

        VersionKey currentKey = ConfigurationManager.getInstance()
                .getCurrentVersion();

        ActivityKey activityKey = context.getActivity();

        VersionKey versionKey = context.getVersion();

        if (activityKey == null || versionKey == null) {
            InternalException ie = new InternalException(Messages.ERROR, "");

            throw new ExceptionMapper().toWebException(ie);
        }

        try {

            GsonBuilder builder = new GsonBuilder();
            builder.setDateFormat(ConfigurationManager.FORMAT_DATE);
            builder.registerTypeHierarchyAdapter(ActivityKey.class,
                    new ActivitySerializer());
            builder.registerTypeHierarchyAdapter(VersionKey.class,
                    new VersionSerializer());
            builder.registerTypeAdapter(Event.class, new EventSerializer(
                    activityKey.getInputEntity().getEntityClass()));
            Gson gson = builder.create();

            VersionedEntity entity = gson.fromJson(reader, genericType);
            entity.updateFrom(versionKey);
            entity.setVersion(currentKey);

            return entity;
        } catch (JsonSyntaxException e) {
            ValidationException ve = new ValidationException(
                    Messages.ERROR_JSON_FORMAT, null, e);

            throw new ExceptionMapper().toWebException(ve);
        } finally {
            reader.close();
        }
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public long getSize(VersionedEntity result, Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(VersionedEntity entity, Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders,
            OutputStream entityStream)
            throws IOException, WebApplicationException {

        OutputStreamWriter writer = new OutputStreamWriter(entityStream,
                ConfigurationManager.CHARSET);

        ConfigurationManager cm = ConfigurationManager.getInstance();

        VersionKey current = cm.getCurrentVersion();
        VersionKey compatible = cm.getCompatibleVersion();

        try {
            GsonBuilder builder = new GsonBuilder();
            builder.setDateFormat(ConfigurationManager.FORMAT_DATE);
            builder.registerTypeHierarchyAdapter(ActivityKey.class,
                    new ActivitySerializer());
            builder.registerTypeHierarchyAdapter(VersionKey.class,
                    new VersionSerializer());
            Gson gson = builder.create();

            entity.convertTo(compatible);
            entity.setVersion(current);
            gson.toJson(entity, genericType, writer);

        } catch (JsonSyntaxException e) {
            InternalException ie = new InternalException(
                    Messages.ERROR_JSON_FORMAT, e);

            throw new ExceptionMapper().toWebException(ie);
        } finally {
            writer.close();
        }
    }
}
