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
import org.apache.kafka.streams.kstream.ForeachAction;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.processor.TopologyBuilder.AutoOffsetReset;
import org.oscm.common.interfaces.data.Event;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.keys.ConsumerKey;
import org.oscm.common.interfaces.services.ConsumerService;
import org.oscm.common.util.ConfigurationManager;
import org.oscm.common.util.ServiceManager;
import org.oscm.common.util.logger.ServiceLogger;

/**
 * Stream class for consumer pipelines with kafka. Takes a event from a topic
 * and processes it.
 * 
 * @author miethaner
 */
public class ConsumerStream extends Stream {

    private static final ServiceLogger LOGGER = ServiceLogger
            .getLogger(ConsumerStream.class);

    private class ConsumerWrapper implements ForeachAction<UUID, Event> {

        @Override
        public void apply(UUID key, Event value) {

            try {
                ConsumerService service = ServiceManager.getInstance()
                        .getConsumerService(consumer);
                service.process(value);
            } catch (ServiceException e) {
                LOGGER.error(e);
            }
        }
    }

    private ConsumerKey consumer;

    /**
     * Creates a new kafka stream for the given consumer key. The stream reads
     * from the input topic defined by the consumer and processes it with the
     * corresponding service.
     * 
     * @param consumer
     *            the consumer key
     */
    public ConsumerStream(ConsumerKey consumer) {

        this.consumer = consumer;
    }

    @Override
    protected KafkaStreams initStreams() {

        UUIDSerializer keySerializer = new UUIDSerializer();
        DataSerializer<Event> inputSerializer = new DataSerializer<>(
                consumer.getInputEntity().getEntityClass());

        KStreamBuilder builder = new KStreamBuilder();

        builder.stream(AutoOffsetReset.EARLIEST, keySerializer, inputSerializer,
                buildEventTopic(consumer.getInputEntity())) //
                .foreach(new ConsumerWrapper());

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
                buildApplicationId(consumer.getConsumerName()));

        return config;
    }
}
