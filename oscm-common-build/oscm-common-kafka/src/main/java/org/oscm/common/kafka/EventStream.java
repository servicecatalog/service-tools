/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 9, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.oscm.common.interfaces.data.Event;
import org.oscm.common.interfaces.services.EventService;
import org.oscm.common.kafka.mappers.EventEventMapper;
import org.oscm.common.util.ServiceConfiguration;

/**
 * @author miethaner
 *
 */
public class EventStream extends Stream {

    private String inputTopic;
    private Class<? extends Event> inputClass;

    private String outputTopic;
    private Class<? extends Event> outputClass;

    private EventService service;

    public EventStream(String inputTopic, Class<? extends Event> inputClass,
            String outputTopic, Class<? extends Event> outputClass,
            EventService service) {
        super();
        this.inputTopic = inputTopic;
        this.inputClass = inputClass;
        this.outputTopic = outputTopic;
        this.outputClass = outputClass;
        this.service = service;
    }

    @Override
    protected KafkaStreams initStream() {

        KStreamBuilder builder = new KStreamBuilder();

        KStream<UUID, Event> stream = builder.stream(new UUIDSerializer(),
                new DataSerializer<>(inputClass), inputTopic);

        stream.flatMap(new EventEventMapper(service)) //
                .through(new UUIDSerializer(),
                        new DataSerializer<>(outputClass), outputTopic); //

        KafkaStreams streams = new KafkaStreams(builder,
                new StreamsConfig(getConfig()));

        return streams;
    }

    private Map<String, Object> getConfig() {

        Map<String, Object> config = new HashMap<>();
        ServiceConfiguration.getInstance()
                .getProprietaryConfig(KafkaConfig.values())
                .forEach((key, value) -> config.put(key, value));

        return config;
    }
}
