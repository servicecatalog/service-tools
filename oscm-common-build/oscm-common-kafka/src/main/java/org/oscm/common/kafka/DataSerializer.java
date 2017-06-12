/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 19, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.oscm.common.interfaces.data.Command;
import org.oscm.common.interfaces.data.Result;
import org.oscm.common.interfaces.data.VersionedEntity;
import org.oscm.common.interfaces.keys.ActivityKey;
import org.oscm.common.interfaces.keys.VersionKey;
import org.oscm.common.util.ServiceConfiguration;
import org.oscm.common.util.serializer.ActivitySerializer;
import org.oscm.common.util.serializer.CommandSerializer;
import org.oscm.common.util.serializer.ResultSerializer;
import org.oscm.common.util.serializer.VersionSerializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Serializer class with gson for kafka producers and consumers.
 * 
 * @author miethaner
 */
public class DataSerializer<D extends VersionedEntity>
        implements Serializer<D>, Deserializer<D>, Serde<D> {

    private Class<? extends D> clazz;
    private VersionKey currentKey;
    private VersionKey compatibleKey;
    private Gson gson;

    public DataSerializer(Class<? extends D> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

        ServiceConfiguration sc = ServiceConfiguration.getInstance();
        this.currentKey = sc.getCurrentVersion();
        this.compatibleKey = sc.getCompatibleVersion();

        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat(ServiceConfiguration.FORMAT_DATE);
        builder.registerTypeAdapter(ActivityKey.class,
                new ActivitySerializer());
        builder.registerTypeAdapter(VersionKey.class, new VersionSerializer());
        builder.registerTypeAdapter(Command.class, new CommandSerializer());
        builder.registerTypeAdapter(Result.class, new ResultSerializer());
        this.gson = builder.create();
    }

    @Override
    public byte[] serialize(String topic, D data) {

        data.convertTo(compatibleKey);
        data.setVersion(currentKey);

        String json = gson.toJson(data);

        return json.getBytes(ServiceConfiguration.CHARSET);
    }

    @Override
    public D deserialize(String topic, byte[] raw) {

        String json = new String(raw, ServiceConfiguration.CHARSET);

        D data = gson.fromJson(json, clazz);
        data.updateFrom(data.getVersion());
        data.setVersion(currentKey);

        return data;
    }

    @Override
    public void close() {
        // ignore
    }

    @Override
    public Deserializer<D> deserializer() {
        return this;
    }

    @Override
    public Serializer<D> serializer() {
        return this;
    }
}
