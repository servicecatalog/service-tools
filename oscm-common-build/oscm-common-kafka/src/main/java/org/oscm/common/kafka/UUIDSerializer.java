/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 1, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

/**
 * Serializer class with UUIDs for kafka producers and consumers.
 * 
 * @author miethaner
 */
public class UUIDSerializer
        implements Serializer<UUID>, Deserializer<UUID>, Serde<UUID> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // ignore
    }

    @Override
    public UUID deserialize(String topic, byte[] data) {

        if (data == null) {
            return null;
        }

        return UUID.nameUUIDFromBytes(data);
    }

    @Override
    public byte[] serialize(String topic, UUID data) {
        return ByteBuffer.allocate(16).putLong(data.getMostSignificantBits())
                .putLong(data.getLeastSignificantBits()).array();
    }

    @Override
    public void close() {
        // ignore
    }

    @Override
    public Deserializer<UUID> deserializer() {
        return this;
    }

    @Override
    public Serializer<UUID> serializer() {
        return this;
    }
}
