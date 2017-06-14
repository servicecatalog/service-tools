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
import org.oscm.common.interfaces.keys.TransitionKey;
import org.oscm.common.kafka.mappers.EventEventMapper;
import org.oscm.common.util.ConfigurationManager;

/**
 * @author miethaner
 *
 */
public class EventStream extends Stream {

    private String inputTopic;
    private String outputTopic;
    private TransitionKey transition;

    public EventStream(String inputTopic, String outputTopic,
            TransitionKey transition) {
        super();
        this.inputTopic = inputTopic;
        this.outputTopic = outputTopic;
        this.transition = transition;
    }

    @Override
    protected KafkaStreams initStreams() {

        KStreamBuilder builder = new KStreamBuilder();

        KStream<UUID, Event> stream = builder.stream(new UUIDSerializer(),
                new DataSerializer<>(transition.getInputClass()), inputTopic);

        stream.flatMap(new EventEventMapper(transition)) //
                .through(new UUIDSerializer(),
                        new DataSerializer<>(transition.getOutputClass()),
                        outputTopic); //

        KafkaStreams streams = new KafkaStreams(builder,
                new StreamsConfig(getConfig()));

        return streams;
    }

    private Map<String, Object> getConfig() {

        Map<String, Object> config = new HashMap<>();
        ConfigurationManager.getInstance()
                .getProprietaryConfig(KafkaConfig.values())
                .forEach((key, value) -> config.put(key, value));

        config.put(APPLICATION_ID,
                buildApplicationId(transition.getTransitionName()));

        return config;
    }
}
