/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jul 11, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.test.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * @author miethaner
 *
 */
public class Producer {

    private KafkaProducer<UUID, Map<String, Object>> producer;

    public Producer(String bootstrap) {

        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);

        producer = new KafkaProducer<>(configs, new UUIDSerializer(),
                new JsonSerializer());
    }

    public void publish(String topic, UUID key, Map<String, Object> value) {

        ProducerRecord<UUID, Map<String, Object>> record = new ProducerRecord<>(
                topic, key, value);

        producer.send(record);
    }

}
