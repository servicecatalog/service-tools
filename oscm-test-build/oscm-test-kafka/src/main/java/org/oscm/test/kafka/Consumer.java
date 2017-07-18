/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jul 11, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.test.kafka;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * @author miethaner
 *
 */
public class Consumer {

    private KafkaConsumer<UUID, Map<String, Object>> consumer;

    public Consumer(String bootstrap) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG,
                UUID.randomUUID().toString());

        this.consumer = new KafkaConsumer<>(configs, new UUIDSerializer(),
                new JsonSerializer());
    }

    public void subscribe(String topic) {
        consumer.subscribe(Arrays.asList(topic));
    }

    public void reset() {
        consumer.seekToBeginning(Collections.emptyList());
    }

    public List<Map<String, Object>> receive() {
        ConsumerRecords<UUID, Map<String, Object>> records = consumer
                .poll(Long.MAX_VALUE);

        List<Map<String, Object>> result = new ArrayList<>();
        records.forEach((r) -> result.add(r.value()));

        return result;
    }
}
