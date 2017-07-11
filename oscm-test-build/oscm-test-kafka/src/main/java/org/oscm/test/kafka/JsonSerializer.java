/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 19, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.test.kafka;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * De-/Serializer class with gson for kafka.
 * 
 * @author miethaner
 */
public class JsonSerializer implements Serializer<Map<String, Object>>,
        Deserializer<Map<String, Object>>, Serde<Map<String, Object>> {

    public static final Charset CHARSET = StandardCharsets.UTF_8;
    public static final String FORMAT_DATE = "yyyy-MM-dd'T'HH:mm:ssXXX";

    private Gson gson;

    public JsonSerializer() {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat(FORMAT_DATE);
        this.gson = builder.create();
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // ignore
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> deserialize(String topic, byte[] raw) {

        if (raw == null) {
            return null;
        }

        String json = new String(raw, CHARSET);

        return gson.fromJson(json, Map.class);
    }

    @Override
    public byte[] serialize(String topic, Map<String, Object> data) {

        return gson.toJson(data).getBytes(CHARSET);
    }

    @Override
    public void close() {
        // ignore
    }

    @Override
    public Serializer<Map<String, Object>> serializer() {
        return this;
    }

    @Override
    public Deserializer<Map<String, Object>> deserializer() {
        return this;
    }
}
