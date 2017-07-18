/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 9, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.processor.TopologyBuilder.AutoOffsetReset;
import org.oscm.common.interfaces.data.Event;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.keys.TransitionKey;
import org.oscm.common.interfaces.services.TransitionService;
import org.oscm.common.util.ConfigurationManager;
import org.oscm.common.util.ServiceManager;
import org.oscm.common.util.logger.ServiceLogger;

/**
 * Stream class for transition pipelines with kafka. Takes a event from a topic
 * and processes it within a mapper. The resulting event is written back to
 * another topic.
 * 
 * @author miethaner
 */
public class TransitionStream extends Stream {

    private static final ServiceLogger LOGGER = ServiceLogger
            .getLogger(TransitionStream.class);

    private class TransitionWrapper implements
            KeyValueMapper<UUID, Event, List<KeyValue<UUID, Event>>> {

        @Override
        public List<KeyValue<UUID, Event>> apply(UUID key, Event value) {

            List<KeyValue<UUID, Event>> eventList = new ArrayList<>();

            try {
                TransitionService service = ServiceManager.getInstance()
                        .getTransitionService(transition);
                List<Event> events = service.process(value);

                events.forEach(
                        (e) -> eventList.add(KeyValue.pair(e.getId(), e)));
            } catch (ServiceException e) {
                LOGGER.error(e);
            }

            return eventList;
        }
    }

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
    public TransitionStream(TransitionKey transition) {

        if (!transition.getOutputEntity().getApplication()
                .equals(ConfigurationManager.getInstance().getSelf())) {
            throw new RuntimeException(
                    "An output entity outside of this application is not allowed");
        }

        this.transition = transition;
    }

    @Override
    protected KafkaStreams initStreams() {

        UUIDSerializer keySerializer = new UUIDSerializer();
        DataSerializer<Event> inputSerializer = new DataSerializer<>(
                transition.getInputEntity().getEntityClass());
        DataSerializer<Event> outputSerializer = new DataSerializer<>(
                transition.getOutputEntity().getEntityClass());

        KStreamBuilder builder = new KStreamBuilder();

        builder.stream(AutoOffsetReset.EARLIEST, keySerializer, inputSerializer,
                buildEventTopic(transition.getInputEntity())) //
                .flatMap(new TransitionWrapper()) //
                .to(keySerializer, outputSerializer,
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
