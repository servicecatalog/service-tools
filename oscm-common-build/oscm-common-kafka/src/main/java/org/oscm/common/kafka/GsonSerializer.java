/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 19, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Serializer class with gson for kafka producers and consumers.
 * 
 * @author miethaner
 */
public class GsonSerializer<R extends Representation>
        implements Serializer<R>, Deserializer<R> {

    public static final Charset CHARSET = StandardCharsets.UTF_8;
    public static final String FORMAT_DATE = "yyyy-MM-dd'T'HH:mm:ssXXX";

    private Class<R> clazz;

    public GsonSerializer(Class<R> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // ignore
    }

    @Override
    public byte[] serialize(String topic, R rep) {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat(FORMAT_DATE);

        if (rep.getVersion() != null) {
            builder.setVersion(rep.getVersion().intValue());
        }

        Gson gson = builder.create();
        String json = gson.toJson(rep);

        return json.getBytes(CHARSET);
    }

    @Override
    public R deserialize(String topic, byte[] data) {

        String json = new String(data, CHARSET);
        Gson gson = new GsonBuilder().setDateFormat(FORMAT_DATE).create();

        return gson.fromJson(json, clazz);
    }

    @Override
    public void close() {
        // ignore
    }
}
