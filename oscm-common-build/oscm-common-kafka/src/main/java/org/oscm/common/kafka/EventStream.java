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
 * Stream class for event pipelines with kafka. Takes a event from a topic and
 * processes it within a mapper. The resulting event is written back to another
 * topic.
 * 
 * @author miethaner
 */
public class EventStream extends Stream {

    private TransitionKey transition;

    /**
     * Creates a new kafka stream for the given transition key. The stream reads
     * from the input topic defined by the transition and processes it with the
     * corresponding service. The resulting event is written to the output
     * topic.
     * 
     * @param transition
     *            the transition key
     */
    public EventStream(TransitionKey transition) {

        if (!transition.getOutputEntity().getApplication()
                .equals(ConfigurationManager.getInstance().getSelf())) {
            throw new RuntimeException(
                    "An output entity outside of this application is not allowed");
        }

        this.transition = transition;
    }

    @Override
    protected KafkaStreams initStreams() {

        KStreamBuilder builder = new KStreamBuilder();

        KStream<UUID, Event> stream = builder.stream(new UUIDSerializer(),
                new DataSerializer<>(
                        transition.getInputEntity().getEntityClass()),
                buildEventTopic(transition.getInputEntity()));

        stream.flatMap(new EventEventMapper(transition)) //
                .to(new UUIDSerializer(),
                        new DataSerializer<>(
                                transition.getOutputEntity().getEntityClass()),
                        buildEventTopic(transition.getOutputEntity())); //

        KafkaStreams streams = new KafkaStreams(builder,
                new StreamsConfig(getConfig()));

        return streams;
    }

    private Map<String, Object> getConfig() {

        Map<String, Object> config = new HashMap<>();
        ConfigurationManager.getInstance()
                .getProprietaryConfig(KafkaConfig.values())
                .forEach((key, value) -> config.put(key, value));

        config.put(StreamsConfig.APPLICATION_ID_CONFIG,
                buildApplicationId(transition.getTransitionName()));

        return config;
    }
}
