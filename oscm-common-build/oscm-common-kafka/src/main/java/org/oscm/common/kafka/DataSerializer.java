/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 19, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka;

import java.util.Date;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.oscm.common.interfaces.data.Command;
import org.oscm.common.interfaces.data.Result;
import org.oscm.common.interfaces.data.Version;
import org.oscm.common.interfaces.data.VersionedEntity;
import org.oscm.common.interfaces.keys.ActivityKey;
import org.oscm.common.util.ConfigurationManager;
import org.oscm.common.util.serializer.ActivitySerializer;
import org.oscm.common.util.serializer.CommandSerializer;
import org.oscm.common.util.serializer.ResultSerializer;
import org.oscm.common.util.serializer.VersionSerializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * De-/Serializer class with gson for kafka.
 * 
 * @author miethaner
 */
public class DataSerializer<D extends VersionedEntity>
        implements Serializer<D>, Deserializer<D>, Serde<D> {

    private Class<? extends D> clazz;
    private Version currentVersion;
    private Version compatibleVersion;
    private Gson gson;

    public DataSerializer(Class<? extends D> clazz) {
        this.clazz = clazz;

        ConfigurationManager sc = ConfigurationManager.getInstance();
        this.currentVersion = sc.getCurrentVersion();
        this.compatibleVersion = sc.getCompatibleVersion();

        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat(ConfigurationManager.FORMAT_DATE);
        builder.registerTypeHierarchyAdapter(ActivityKey.class,
                new ActivitySerializer());
        builder.registerTypeHierarchyAdapter(Version.class,
                new VersionSerializer());
        builder.registerTypeAdapter(Command.class, new CommandSerializer());
        builder.registerTypeAdapter(Result.class, new ResultSerializer());
        this.gson = builder.create();
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // ignore
    }

    @Override
    public byte[] serialize(String topic, D data) {

        data.convertTo(compatibleVersion);
        data.setVersion(currentVersion);

        data.setTimestamp(new Date());

        String json = gson.toJson(data);

        return json.getBytes(ConfigurationManager.CHARSET);
    }

    @Override
    public D deserialize(String topic, byte[] raw) {

        if (raw == null) {
            return null;
        }

        String json = new String(raw, ConfigurationManager.CHARSET);

        D data = gson.fromJson(json, clazz);

        if (data != null) {
            data.updateFrom(data.getVersion());
            data.setVersion(currentVersion);
        }

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
