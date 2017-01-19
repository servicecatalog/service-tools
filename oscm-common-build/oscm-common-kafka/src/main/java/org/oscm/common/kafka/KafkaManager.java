/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 19, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.oscm.common.interfaces.config.VersionKey;

/**
 * Singleton class for managing kafka connections.
 * 
 * @author miethaner
 */
public class KafkaManager {

    private static KafkaManager cm;

    /**
     * Returns the singleton instance of the kafka manager.
     * 
     * @return the kafka manager
     */
    public static KafkaManager getInstance() {
        if (cm == null) {
            throw new RuntimeException("Kafka connection not initialized");
        }

        return cm;
    }

    /**
     * Initializes the connection manager with the given configurations. This
     * overwrites the previous connection manager instance.
     * 
     * @param consumerConfig
     *            the configuration for all consumers
     * @param producerConfig
     *            the configuration for all producers
     */
    public static void init(VersionKey version,
            Map<String, String> consumerConfig,
            Map<String, String> producerConfig) {
        cm = new KafkaManager(version, consumerConfig, producerConfig);
    }

    private Map<Class<?>, KafkaProducer<Long, ?>> producerMap;
    private VersionKey version;
    private Map<String, String> consumerConfig;
    private Map<String, String> producerConfig;

    private KafkaManager(VersionKey version, Map<String, String> consumerConfig,
            Map<String, String> producerConfig) {
        this.producerMap = new ConcurrentHashMap<>();
        this.version = version;
        this.consumerConfig = consumerConfig;
        this.producerConfig = producerConfig;
    }

    /**
     * Gets the version key all published records have to be compatible to.
     * 
     * @return the version key
     */
    public VersionKey getCompatibilityVersion() {
        return version;
    }

    /**
     * Gets a new Instance of a kafka consumer to be used in a single thread.
     * 
     * @param clazz
     *            the value class of the kafka record
     * @return the kafka consumer
     */
    public <R extends Representation> KafkaConsumer<Long, R> getConsumer(
            Class<R> clazz) {

        return new KafkaConsumer<>(new HashMap<>(consumerConfig),
                new LongDeserializer(), new GsonSerializer<>(clazz));
    }

    /**
     * Gets the current kafka producer for the given class.
     * 
     * @param clazz
     *            the value class for the kafka record
     * @return the kafka producer
     */
    @SuppressWarnings("unchecked")
    public <R extends Representation> KafkaProducer<Long, R> getProducer(
            Class<R> clazz) {

        if (!producerMap.containsKey(clazz)) {
            producerMap.put(clazz,
                    new KafkaProducer<>(new HashMap<>(producerConfig),
                            new LongSerializer(), new GsonSerializer<>(clazz)));
        }

        return (KafkaProducer<Long, R>) producerMap.get(clazz);
    }
}
