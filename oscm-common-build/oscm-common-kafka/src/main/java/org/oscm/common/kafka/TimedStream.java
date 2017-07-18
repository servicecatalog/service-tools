/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 9, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.processor.TopologyBuilder.AutoOffsetReset;
import org.oscm.common.interfaces.data.Event;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.keys.TransitionKey;
import org.oscm.common.interfaces.services.TransitionService;
import org.oscm.common.util.ConfigurationManager;
import org.oscm.common.util.ServiceManager;
import org.oscm.common.util.logger.ServiceLogger;

/**
 * Stream class for scheduled transition pipelines with kafka. Takes a event
 * from a topic and adds it to scheduler that processes it periodically. The
 * resulting events are written back to another topic.
 * 
 * @author miethaner
 */
public class TimedStream extends Stream {

    private static final int POOL_SIZE = 4;
    private static final String STORE_NAME = "event_store";

    private static final ServiceLogger LOGGER = ServiceLogger
            .getLogger(TimedStream.class);

    private ScheduledExecutorService executor;
    private Map<UUID, ScheduledFuture<?>> tasks;

    private KafkaProducer<UUID, Event> producer;
    private String outputTopic;

    private TransitionKey transition;
    private long interval;

    /**
     * Creates a new kafka stream for the given transition key. The stream reads
     * from the input topic defined by the transition and add adds tasks per
     * event to a scheduler. The tasks calls the corresponding service with its
     * event. The resulting events are written to the output topic.
     * 
     * @param transition
     *            the transition key
     * @param interval
     *            the delay between task execution in milliseconds
     */
    public TimedStream(TransitionKey transition, long interval) {

        if (!transition.getOutputEntity().getApplication()
                .equals(ConfigurationManager.getInstance().getSelf())) {
            throw new RuntimeException(
                    "An output entity outside of this application is not allowed");
        }

        this.tasks = new ConcurrentHashMap<>();
        this.transition = transition;
        this.interval = interval;
    }

    @Override
    protected KafkaStreams initStreams() {

        UUIDSerializer keySerializer = new UUIDSerializer();
        DataSerializer<Event> inputSerializer = new DataSerializer<>(
                transition.getInputEntity().getEntityClass());
        DataSerializer<Event> outputSerializer = new DataSerializer<>(
                transition.getOutputEntity().getEntityClass());

        KStreamBuilder builder = new KStreamBuilder();

        builder.table(AutoOffsetReset.EARLIEST, keySerializer, inputSerializer,
                buildEventTopic(transition.getInputEntity()), STORE_NAME)
                .foreach(this::execute);

        executor = Executors.newScheduledThreadPool(POOL_SIZE);

        outputTopic = buildEventTopic(transition.getOutputEntity());

        producer = new KafkaProducer<>(getConfig(), keySerializer,
                outputSerializer);

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

    private void execute(UUID key, Event value) {

        if (tasks.containsKey(key)) {
            tasks.get(key).cancel(false);
        }

        Runnable runner = () -> {
            try {

                TransitionService service = ServiceManager.getInstance()
                        .getTransitionService(transition);
                List<Event> events = service.process(value);

                if (events == null) {
                    throw new RuntimeException("Remove Task");
                }

                events.forEach((e) -> producer
                        .send(new ProducerRecord<>(outputTopic, e.getId(), e)));
            } catch (ServiceException e) {
                LOGGER.error(e);
            } catch (Exception e) {
                LOGGER.exception(e);
                throw e;
            }

        };

        ScheduledFuture<?> future = executor.scheduleWithFixedDelay(runner, 0,
                interval, TimeUnit.MILLISECONDS);

        tasks.put(key, future);

    }
}
